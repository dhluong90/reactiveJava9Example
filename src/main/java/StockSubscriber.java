import java.util.concurrent.Flow;

public class StockSubscriber implements Flow.Subscriber<Double> {
    private Flow.Subscription subscription;

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        System.out.println("on Subscribe");
    }

    @Override
    public void onNext(Double item) {
        System.out.println("on Next" + item);
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("on Error");
    }

    @Override
    public void onComplete() {
        System.out.println("on Complete");
    }
}
