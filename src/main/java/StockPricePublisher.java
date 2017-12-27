import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

public class StockPricePublisher implements Publisher<Double> {

	private static final String LOG_MESSAGE_FORMAT = "Publisher >> [%s] %s%n";

	final ExecutorService executor = Executors.newFixedThreadPool(4);
	private List<MySubscription> subscriptions = Collections.synchronizedList(new ArrayList<MySubscription>());

	private final CompletableFuture<Void> terminated = new CompletableFuture<>();

	@Override
	public void subscribe(Subscriber<? super Double> subscriber) {
		MySubscription subscription = new MySubscription(subscriber, executor);

		subscriptions.add(subscription);

		subscriber.onSubscribe(subscription);
	}

	private class MySubscription implements Subscription {

		private final ExecutorService executor;

		private Subscriber<? super Double> subscriber;
		private AtomicBoolean isCanceled;

		public MySubscription(Subscriber<? super Double> subscriber, ExecutorService executor) {
			this.subscriber = subscriber;
			this.executor = executor;

			isCanceled = new AtomicBoolean(false);
		}

		@Override
		public void request(long n) {
			if (isCanceled.get())
				return;

			if (n < 0)
				executor.execute(() -> subscriber.onError(new IllegalArgumentException()));
			else
				publishItems(n);
		}

		@Override
		public void cancel() {
			isCanceled.set(true);

			synchronized (subscriptions) {
				subscriptions.remove(this);
				if (subscriptions.size() == 0)
					shutdown();
			}
		}

		private void publishItems(long n) {
			for (int i = 0; i < n; i++) {

				executor.execute(() -> {
					Double value = StockPriceService.getInstance().fetchStockPrice();
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							// Here, we can safely update the GUI
							// because we'll be called from the
							// event dispatch thread
							subscriber.onNext(value);
						}
					});

				});
			}
		}

		private void shutdown() {
			LogService.log("Shut down executor...");
			executor.shutdown();
			newSingleThreadExecutor().submit(() -> {

				LogService.log("Shutdown complete.");
				terminated.complete(null);
			});
		}

	}

}