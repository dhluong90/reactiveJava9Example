import java.util.List;

import javax.swing.SwingWorker;

/**
 * Creates a real-time chart using SwingWorker
 */
public class SwingWorkerRealTimeApp {

/*  MySwingWorker mySwingWorker;
  SwingWrapper<XYChart> sw;
  XYChart chart;*/

  public SwingWorkerRealTimeApp(StockChartUIComponent stockChartUIComponent) {
    this.stockChartUIComponent = stockChartUIComponent;
  }

  StockChartUIComponent stockChartUIComponent;

  public static void main(String[] args) throws Exception {
    StockChartUIComponent stockChartUIComponent = new StockChartUIComponent();
    stockChartUIComponent.display();
    SwingWorkerRealTimeApp swingWorkerRealTimeApp = new SwingWorkerRealTimeApp(stockChartUIComponent);
    swingWorkerRealTimeApp.go();
  }

  private void go() {
    MySwingWorker mySwingWorker = new MySwingWorker(this.stockChartUIComponent);
    mySwingWorker.execute();
  }

  private class MySwingWorker extends SwingWorker<Boolean, Double> {

    StockChartUIComponent stockChartUIComponent;

    public MySwingWorker(StockChartUIComponent stockChartUIComponent) {
      this.stockChartUIComponent = stockChartUIComponent;
    }

    @Override
    protected Boolean doInBackground() throws Exception {

      while (!isCancelled()) {
        publish(StockPriceService.getInstance().fetchStockPrice());
        try {
          Thread.sleep(5);
        } catch (InterruptedException e) {
          // eat it. caught when interrupt is called
          System.out.println("MySwingWorker shut down.");
        }

      }

      return true;
    }

    @Override
    protected void process(List<Double> chunks) {
      long start = System.currentTimeMillis();
      stockChartUIComponent.addItemtAndUpdateUi(chunks);
      long duration = System.currentTimeMillis() - start;
      try {
        Thread.sleep(40 - duration); // 40 ms ==> 25fps
        // Thread.sleep(400 - duration); // 40 ms ==> 2.5fps
      } catch (InterruptedException e) {
      }

    }
  }
}