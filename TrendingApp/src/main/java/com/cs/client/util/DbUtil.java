package com.cs.client.util;

import com.cs.client.enumType.Cassette;
import com.cs.client.enumType.CassetteDataPoint;

public class DbUtil {

	/**
	 * Gets the field name
	 * 
	 * @return
	 */
	public static String getFieldName(Cassette source, CassetteDataPoint dataPoint) {
		StringBuffer sb = new StringBuffer();
		sb.append(source.name());
		sb.append("_");
		sb.append(dataPoint.name());

		return sb.toString();
	}

	public static void main(String[] args) {
		System.out.println(getFieldName(Cassette.CAS1, CassetteDataPoint.COLD1_TEMP));
		System.out.println(getFieldName(Cassette.CAS2, CassetteDataPoint.HOT1_TEMP));
	}

}
