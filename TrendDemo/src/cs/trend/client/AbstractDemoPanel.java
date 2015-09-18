package cs.trend.client;

import javax.swing.JPanel;

import org.jfree.data.time.TimeSeries;

public abstract class AbstractDemoPanel implements DemoPanel {

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void startTrending() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopTrending() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDomainRange(int minutes) {
		// TODO Auto-generated method stub

	}

	@Override
	public JPanel getComponent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TimeSeries addPlot(Source source, CassetteDataPoint dataPoint) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removePlot() {
		// TODO Auto-generated method stub

	}

}
