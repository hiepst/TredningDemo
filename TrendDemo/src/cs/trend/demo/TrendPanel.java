package cs.trend.demo;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDifferenceRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.Layer;
import org.jfree.ui.LengthAdjustmentType;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;

public class TrendPanel extends JPanel implements DemoPanel, OutOfLimitListener {

	private static final long serialVersionUID = 2015090701;

	private Timer timer;

	// / ** The datasets. */
	private TimeSeriesCollection dataset;

	private XYPlot plot;

	private ChartPanel chartPanel;

	private XYItemRenderer separatePlotRenderer;

	private DatasetDao dao;

	public void setDao(DatasetDao dao) {
		this.dao = dao;
	}

	private XYDifferenceRenderer plotDifferenceRenderer;

	private JFreeChart chart;

	private List<ValueMarker> valueMarkers;

	@SuppressWarnings("deprecation")
	public void init() {
		valueMarkers = new ArrayList<ValueMarker>();

		plotDifferenceRenderer = new XYDifferenceRenderer(Color.green,
				Color.red, false);
		plotDifferenceRenderer.setRoundXCoordinates(true);

		dao.setOutOfLimitListener(this);

		this.dataset = dao.getDataSet();

		chart = ChartFactory.createTimeSeriesChart("Trending", // title
				"Time", // x-axis label
				"Value", // y-axis label
				dataset, // data
				true, // create legend?
				true, // generate tooltips?
				false // generate URLs?
				);
		chart.setBorderPaint(Color.black);
		chart.setBorderVisible(true);
		chart.setBackgroundPaint(Color.white);

		plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		plot.setDomainPannable(true);
		plot.setRangePannable(true);

		separatePlotRenderer = plot.getRenderer(0);
		separatePlotRenderer.setSeriesPaint(0, Color.green);
		separatePlotRenderer.setSeriesPaint(1, Color.blue);
		separatePlotRenderer.setSeriesPaint(2, Color.cyan);
		separatePlotRenderer.setSeriesPaint(3, Color.magenta);

		// plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 4, 4, 4, 4));
		setDomainRange(5);

		configRangeAxis();

		chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
		chartPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		setLayout(new BorderLayout());
		add(chartPanel);

		timer = new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addData();
			}
		});
	}

	public void setChartTitle(String text) {
		chart.setTitle(text);
	}

	public void setDomainRange(int minutes) {
		ValueAxis axis = plot.getDomainAxis();
		axis.setAutoRange(true);
		axis.setFixedAutoRange(minutes * 60000.0); // 5 * 60 seconds
	}

	public void addLimitMarker(String label, Color color, double value) {
		ValueMarker marker = new ValueMarker(value);
		marker.setLabelOffsetType(LengthAdjustmentType.EXPAND);
		marker.setPaint(color);
		marker.setStroke(new BasicStroke(2.0f));
		marker.setLabel(label);
		marker.setLabelFont(new Font("SansSerif", Font.PLAIN, 11));
		marker.setLabelPaint(color);
		marker.setLabelAnchor(RectangleAnchor.TOP_LEFT);
		marker.setLabelTextAnchor(TextAnchor.BOTTOM_LEFT);

		valueMarkers.add(marker);
		plot.addRangeMarker(marker);
	}

	private void configRangeAxis() {
		ValueAxis rangeAxis = plot.getRangeAxis();
		rangeAxis.setFixedAutoRange(14);
		rangeAxis.setLowerBound(18);
		rangeAxis.setUpperBound(28);
	}

	public void startTrending() {
		timer.start();
	}

	public void stopTrending() {
		timer.stop();
	}

	/**
	 * Handles a click on the button by adding new (random) data.
	 *
	 * @param e
	 *            the action event.
	 */
	synchronized private void addData() {
		dao.addDatas();
	}

	public void showLimitSeries(boolean enabled) {
		if (enabled) {
			for (ValueMarker marker : valueMarkers) {
				plot.addRangeMarker(marker);
			}
		} else {
			for (ValueMarker marker : valueMarkers) {
				plot.removeRangeMarker(marker);
			}
		}
	}

	@Override
	public JPanel getComponent() {
		return this;
	}

	@Override
	public void addPlot(CassetteDataPoint dataPoint) {
		if (dataPoint == null) {
			return;
		}

		dao.addSeries(dataPoint);
	}

	@Override
	public void removePLot() {
		dao.removeSeries();
	}

	@Override
	public void outOfLimitPerform(Instant begin, Instant end, double value) {
		Marker cooling = new IntervalMarker(begin.getNano() * 1000,
				end.getNano() * 1000);
		cooling.setLabelOffsetType(LengthAdjustmentType.EXPAND);
		cooling.setPaint(Color.orange);
		cooling.setLabel("Out Of Limit");
		cooling.setLabelFont(new Font("SansSerif", Font.PLAIN, 11));
		cooling.setLabelPaint(Color.orange);
		cooling.setLabelAnchor(RectangleAnchor.TOP_LEFT);
		cooling.setLabelTextAnchor(TextAnchor.TOP_RIGHT);
		plot.addDomainMarker(cooling, Layer.BACKGROUND);
	}

	@Override
	public void showPlotDifference() {
		plot.setRenderer(plotDifferenceRenderer);

	}

	@Override
	public void showPlotsSeparately() {
		plot.setRenderer(separatePlotRenderer);
	}

	@Override
	public int getPlotCount() {
		return dao.getDataSet().getSeriesCount();
	}

}
