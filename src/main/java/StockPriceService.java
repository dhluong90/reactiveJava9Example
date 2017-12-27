public class StockPriceService {

    private static final StockPriceService stockPriceService = new StockPriceService();
    private Double previousNumber = 0.0;

    private StockPriceService() {

    }

    public static StockPriceService getInstance() {
        return stockPriceService;
    }

    public Double fetchStockPrice() {
        LogService.log("call service on:");
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Double value = Math.random();
        previousNumber = value;
        return (previousNumber + value) / 2;
    }
}
