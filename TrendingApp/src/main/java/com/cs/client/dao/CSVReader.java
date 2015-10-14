package com.cs.client.dao;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CSVReader {

	// Hot 1 temperature indexes.

	public static final int CAS1_HOT1_TEMP_INDEX = 56;

	public static final int CAS2_HOT1_TEMP_INDEX = 57;

	public static final int CAS3_HOT1_TEMP_INDEX = 58;

	public static final int CAS4_HOT1_TEMP_INDEX = 59;

	public static final int CAS5_HOT1_TEMP_INDEX = 60;

	public static final int CAS6_HOT1_TEMP_INDEX = 61;

	public static final int CAS7_HOT1_TEMP_INDEX = 62;

	public static final int CAS8_HOT1_TEMP_INDEX = 63;

	public static final int CAS9_HOT1_TEMP_INDEX = 64;

	public static final int CAS10_HOT1_TEMP_INDEX = 65;

	// Hot 2 temperature indexes
	public static final int CAS1_HOT2_TEMP_INDEX = 66;

	public static final int CAS2_HOT2_TEMP_INDEX = 67;

	public static final int CAS3_HOT2_TEMP_INDEX = 68;

	public static final int CAS4_HOT2_TEMP_INDEX = 69;

	public static final int CAS5_HOT2_TEMP_INDEX = 70;

	public static final int CAS6_HOT2_TEMP_INDEX = 71;

	public static final int CAS7_HOT2_TEMP_INDEX = 72;

	public static final int CAS8_HOT2_TEMP_INDEX = 73;

	public static final int CAS9_HOT2_TEMP_INDEX = 74;

	public static final int CAS10_HOT2_TEMP_INDEX = 75;

	private String csvFile;

	public CSVReader(String csvFile) {
		this.csvFile = csvFile;
	}

	public static void main(String[] args) {

		CSVReader reader = new CSVReader("res/ScienceData_FLIGHT.csv");
		reader.getDoubleData(CAS3_HOT1_TEMP_INDEX, 10);
		System.out.println();
		reader.getDoubleData(CAS3_HOT2_TEMP_INDEX, 10);
	}

	public double[] getDoubleData(int index, int itemCount) {
		double[] data = new double[itemCount];

		int count = 0;
		// csvFile = "/Users/hvhong/Documents/Trending
		// App/ScienceData_FLIGHT.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {

			br = new BufferedReader(new FileReader(csvFile));

			// Skip the header
			br.readLine();

			while ((line = br.readLine()) != null) {
				// use comma as separator

				String[] csvColumns = line.split(cvsSplitBy);
				double parseDouble = Double.parseDouble(csvColumns[index]);
				data[count++] = parseDouble;

				System.out.println(parseDouble);

				if (count == itemCount) {
					return data;
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		System.out.println("Done - Total datat items: " + count);

		return data;
	}

	public void run() {
		int count = 0;
		// csvFile = "/Users/hvhong/Documents/Trending
		// App/ScienceData_FLIGHT.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {

			br = new BufferedReader(new FileReader(csvFile));

			while ((line = br.readLine()) != null) {
				// use comma as separator
				displayColumns(line, cvsSplitBy);
				count++;
				if (count == 5) {
					return;
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		System.out.println("Done");
	}

	private void displayColumns(String line, String cvsSplitBy) {
		String[] csvColumns = line.split(cvsSplitBy);
		for (String h : csvColumns) {
			System.out.print(h + "__");
		}

		System.out.println();
	}

}
