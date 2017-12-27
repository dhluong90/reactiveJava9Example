import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import java.util.LinkedList;
import java.util.List;

public class StockChartUIComponent {

    SwingWrapper<XYChart> sw;
    XYChart chart;
    List<Double> dataSet = new LinkedList();
    int maximunDataSet = 500;

    public StockChartUIComponent() {
        // Create Chart
        chart = QuickChart.getChart("SwingWorker XChart Real-time Demo", "Time", "Value", "randomWalk", new double[] { 0 }, new double[] { 0 });
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setXAxisTicksVisible(false);

        // Show it
        sw = new SwingWrapper<XYChart>(chart);
    }


    public void display() {
        sw.displayChart();
    }

    public void addItemtAndUpdateUi(Double item) {
        if (dataSet.size() == 500) {
            dataSet.remove(0);
        }
        dataSet.add(item);
        chart.updateXYSeries("randomWalk", null, dataSet, null);
        sw.repaintChart();
        LogService.log("Update Ui ");
    }

    public void addItemtAndUpdateUi(List<Double> items) {
        if (items == null) throw new NullPointerException();
        dataSet.addAll(items);
        if (dataSet.size() > 500) {
            dataSet.subList(dataSet.size() - 500, dataSet.size());
        }
        chart.updateXYSeries("randomWalk", null, dataSet, null);
        sw.repaintChart();
        LogService.log("Update Ui ");
    }
}
