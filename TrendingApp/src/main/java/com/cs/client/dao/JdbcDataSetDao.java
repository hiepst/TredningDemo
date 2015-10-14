package com.cs.client.dao;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.jdbc.JDBCXYDataset;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import com.cs.client.OutOfLimitListener;
import com.cs.client.util.ui.UiUtil;

public class JdbcDataSetDao implements DatasetDao {

	private Properties dbProperties;

	public JdbcDataSetDao() {
		dbProperties = new Properties();
		try {
			dbProperties.load(new FileInputStream("res/db.properties"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public TimeSeriesCollection getDataSet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TimeSeriesCollection getDataSet(String displayName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeSeries(String displayName) {
		// TODO Auto-generated method stub

	}

	@Override
	public TimeSeries addSeries(String displayName) {
		// TODO Auto-generated method stub
		return null;

	}

	@Override
	public void addDatas() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addData(String displayName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOutOfLimitListener(OutOfLimitListener outOfLimitListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addMovingAverage(int periodCount, int skip) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeMovingAverage() {
		// TODO Auto-generated method stub

	}

	public JDBCXYDataset readData() {

		JDBCXYDataset data = null;
		String url = dbProperties.getProperty("url");
		String user = dbProperties.getProperty("user");
		String password = dbProperties.getProperty("password");
		Connection con;
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.err.print("ClassNotFoundException: ");
			System.err.println(e.getMessage());
		}

		try {
			con = DriverManager.getConnection(url, user, password);
			data = new JDBCXYDataset(con);
			String sql = "SELECT timestamp," + "cas1hot1temperature, cas2hot1temperature, cas3hot1temperature "
					+ "FROM sciencedata;";

			data.executeQuery(sql);
			con.close();
		} catch (SQLException e) {
			System.err.print("SQLException: ");
			System.err.println(e.getMessage());
		} catch (Exception e) {
			System.err.print("Exception: ");
			System.err.println(e.getMessage());
		}
		return data;
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, you can set the GUI to another look
			// and feel.
		}

		// Create and set up the window.
		JFrame frame = new JFrame("JDBC Dataset Dao");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JdbcDataSetDao dao = new JdbcDataSetDao();
		JDBCXYDataset dataset = dao.readData();
		System.out.println("Series count = " + dataset.getSeriesCount());
		System.out.println("Item count = " + dataset.getItemCount());

		JFreeChart seriesChart = ChartFactory.createTimeSeriesChart("Testing", "Timestamp", "Value", dataset, false,
				false, false);
		ChartPanel chartPanel = new ChartPanel(seriesChart);
		chartPanel.setPreferredSize(new Dimension(500, 500));

		frame.getContentPane().add(chartPanel, BorderLayout.CENTER);

		// Display the window.
		UiUtil.centerAndShow(frame);
	}

	public static void main(String[] args) {

		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				createAndShowGUI();
			}
		});
	}

}
