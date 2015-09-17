package cs.trend.common;

import cs.trend.client.CassetteDataPoint;
import cs.trend.client.Source;

public class DbUtil {

	/**
	 * Gets the field name
	 * 
	 * @return
	 */
	public static String getFieldName(Source source, CassetteDataPoint dataPoint) {
		StringBuffer sb = new StringBuffer();
		sb.append(source.name());
		sb.append("_");
		sb.append(dataPoint.name());

		return sb.toString();
	}

	public static void main(String[] args) {
		System.out.println(getFieldName(Source.CAS1, CassetteDataPoint.COLD1_TEMPERATURE));
		System.out.println(getFieldName(Source.CAS2, CassetteDataPoint.HOT1_TEMPERATURE));
	}

}
