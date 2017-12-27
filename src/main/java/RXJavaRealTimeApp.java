import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

public class RXJavaRealTimeApp {

    public static void main(String[] args) {
        StockChartUIComponent stockChartUIComponent = new StockChartUIComponent();
        stockChartUIComponent.display();
        Flowable.fromCallable(() -> StockPriceService.getInstance().fetchStockPrice())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .repeat()
                .subscribe((items) -> {
                    stockChartUIComponent.addItemtAndUpdateUi(items);
                }, Throwable::printStackTrace);
    }
}
