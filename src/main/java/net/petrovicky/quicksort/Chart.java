package net.petrovicky.quicksort;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Chart {

    private static final int CHART_SIZE_X = 1280;
    private static final int CHART_SIZE_Y = 720;

    private static final Logger LOGGER = LoggerFactory.getLogger(Chart.class);

    /**
     * Write a chart containing specified data into a given file.
     * 
     * This is more of a utility method than a class, but what the hell...
     * 
     * @param target
     *            Target file for the chart.
     * @param data
     *            Data to render. Keys are data series, values represent X,Y data points in the chart.
     */
    public void chart(final File target, final Map<String, Map<Integer, Integer>> data) {
        // prepare data
        final XYSeriesCollection dataset = new XYSeriesCollection();
        for (final Map.Entry<String, Map<Integer, Integer>> entry : data.entrySet()) {
            final XYSeries xy = new XYSeries(entry.getKey());
            for (final Map.Entry<Integer, Integer> pair : entry.getValue().entrySet()) {
                xy.add(pair.getKey(), pair.getValue());
            }
            dataset.addSeries(xy);
        }
        // prepare axes
        final LogAxis xAxis = new LogAxis("Array size");
        xAxis.setBase(10);
        final NumberAxis yAxis = new NumberAxis("Time per item [ns]");
        // prepare chart
        final XYPlot plot = new XYPlot(dataset, xAxis, yAxis, new XYLineAndShapeRenderer(true, true));
        final JFreeChart chart = new JFreeChart("Quicksort Benchmarks", JFreeChart.DEFAULT_TITLE_FONT, plot, true);
        // draw chart
        try {
            if (target.exists()) {
                target.delete();
            }
            ChartUtilities.saveChartAsPNG(target, chart, Chart.CHART_SIZE_X, Chart.CHART_SIZE_Y, null);
            Chart.LOGGER.info("Chart written as {}.", target.getAbsolutePath());
        } catch (final IOException e) {
            Chart.LOGGER.warn("Cannot write chart file.", e);
        }
    }

}
