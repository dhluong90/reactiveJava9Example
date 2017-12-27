import java.util.concurrent.Flow;

public class JavaFlowRealTimeApp {

    public static void main(String[] args) {
        final StockChartUIComponent stockChartUIComponent = new StockChartUIComponent();
        stockChartUIComponent.display();
        StockPricePublisher stockPricePublisher = new StockPricePublisher();
        stockPricePublisher.subscribe(new Flow.Subscriber<Double>() {
            Flow.Subscription subscription;
            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                LogService.log("onSubribe from Subscriber");
                subscription.request(1);
                this.subscription = subscription;
            }

            @Override
            public void onNext(Double item) {
                stockChartUIComponent.addItemtAndUpdateUi(item);
                subscription.request(1);

            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onComplete() {
                LogService.log("Done");
            }
        });
    }
}
