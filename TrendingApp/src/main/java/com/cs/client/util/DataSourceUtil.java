package com.cs.client.util;

import com.cs.client.enumType.Cassette;
import com.cs.client.enumType.CassetteDataPoint;

public class DataSourceUtil {
	private static final String NAME_SEPARATOR = "-";

	public static String getDisplayName(Cassette source, CassetteDataPoint dataPoint) {
		if (source == null) {
			return dataPoint.toString();
		}

		if (dataPoint == null) {
			return source.toString();
		}

		StringBuffer sb = new StringBuffer();
		sb.append(source.toString());
		sb.append(NAME_SEPARATOR);
		sb.append(dataPoint.toString());

		return sb.toString();
	}

	public static Cassette getSource(String fromName) {
		String[] names = fromName.split(NAME_SEPARATOR);
		String sourceName = names[0];
		for (Cassette s : Cassette.values()) {
			if (s.toString().equals(sourceName.trim())) {
				return s;
			}
		}

		return null;
	}

	public static CassetteDataPoint getCassetteDataPoint(String fromName) {
		String[] names = fromName.split(NAME_SEPARATOR);
		String dataPointName = names[0];
		if (names.length > 1) {
			dataPointName = names[1];
		}
		for (CassetteDataPoint dp : CassetteDataPoint.values()) {
			if (dp.toString().equals(dataPointName.trim())) {
				return dp;
			}
		}

		return null;
	}

	public static void main(String[] args) {
		Cassette source = getSource("Cassette 2");
		System.out.println(source);

		source = getSource("Cassette 3-Hot 1 Temp");
		System.out.println(source);

		source = getSource("Cassette 4   - something");
		System.out.println(source);

		CassetteDataPoint cassetteDataPoint = getCassetteDataPoint(CassetteDataPoint.HOT1_TEMP.toString());
		System.out.println(cassetteDataPoint);

		cassetteDataPoint = getCassetteDataPoint("something-" + CassetteDataPoint.HOT2_TEMP.toString());
		System.out.println(cassetteDataPoint);

		cassetteDataPoint = getCassetteDataPoint(
				Cassette.CAS1.toString() + " - " + CassetteDataPoint.COLD1_TEMP.toString());
		System.out.println(cassetteDataPoint);

	}

}
