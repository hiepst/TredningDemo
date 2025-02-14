package cs.trend.client;

import javax.swing.JPanel;

import org.jfree.data.time.TimeSeries;

public interface DemoPanel {

	void init();

	void startTrending();

	void stopTrending();

	JPanel getComponent();

	TimeSeries addPlot(String displayName);

	void removePlot(String displayName);

	public void setDomainRange(int minutes);

	void showPlotDifference();

	void showPlotsSeparately();

	int getPlotCount();

	void showAveragePlot(int periodCount, int skip);

	void hideAveragePlot();
}
