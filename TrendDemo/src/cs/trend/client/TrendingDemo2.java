package cs.trend.client;

/* --------------------------
 * CrosshairOverlayDemo1.java
 * --------------------------
 * (C) Copyright 2003-2014, by Object Refinery Limited.
 *
 */



import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleEdge;

/**
 * A demo showing crosshairs that follow the data points on an XYPlot.
 */
public class TrendingDemo2 extends JFrame {
  
    static class MyDemoPanel extends JPanel implements ChartMouseListener {
        
        private ChartPanel chartPanel;
    
        private Crosshair xCrosshair;
    
        private Crosshair yCrosshair;
    
        public MyDemoPanel() {
            super(new BorderLayout());
            JFreeChart chart = createChart(createDataset());
            this.chartPanel = new ChartPanel(chart);
            this.chartPanel.addChartMouseListener(this);
            CrosshairOverlay crosshairOverlay = new CrosshairOverlay();
            this.xCrosshair = new Crosshair(Double.NaN, Color.GRAY, 
                    new BasicStroke(0f));
            this.xCrosshair.setLabelVisible(true);
            this.yCrosshair = new Crosshair(Double.NaN, Color.GRAY, 
                    new BasicStroke(0f));
            this.yCrosshair.setLabelVisible(true);
            crosshairOverlay.addDomainCrosshair(xCrosshair);
            crosshairOverlay.addRangeCrosshair(yCrosshair);
            this.chartPanel.addOverlay(crosshairOverlay);
            add(this.chartPanel);
        }
    
        private JFreeChart createChart(XYDataset dataset) {
            JFreeChart chart = ChartFactory.createXYLineChart(
                    "Demo", "X", "Y", dataset);
            return chart;
        }

        private XYDataset createDataset() {
            XYSeries series = new XYSeries("S1");
            for (int x = 0; x < 10; x++) {
                series.add(x, x + Math.random() * 4.0);
            }
            XYSeriesCollection dataset = new XYSeriesCollection(series);
            return dataset;
        }
    
        @Override
        public void chartMouseClicked(ChartMouseEvent event) {
            // ignore
        }

        @Override
        public void chartMouseMoved(ChartMouseEvent event) {
            Rectangle2D dataArea = this.chartPanel.getScreenDataArea();
            JFreeChart chart = event.getChart();
            XYPlot plot = (XYPlot) chart.getPlot();
            ValueAxis xAxis = plot.getDomainAxis();
            double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea, 
                    RectangleEdge.BOTTOM);
            // make the crosshairs disappear if the mouse is out of range
            if (!xAxis.getRange().contains(x)) { 
                x = Double.NaN;                  
            }
            double y = DatasetUtilities.findYValue(plot.getDataset(), 0, x);
            this.xCrosshair.setValue(x);
            this.yCrosshair.setValue(y);
        }  
        
    }

    public TrendingDemo2(String title) {
        super(title);
        setContentPane(createDemoPanel());
    }

    /**
     * Creates a panel for the demo (used by SuperDemo.java).
     *
     * @return A panel.
     */
    public static JPanel createDemoPanel() {
        return new MyDemoPanel();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	TrendingDemo2 app = new TrendingDemo2(
                        "JFreeChar");
                app.pack();
                app.setVisible(true);
            }
        });
    }

}
