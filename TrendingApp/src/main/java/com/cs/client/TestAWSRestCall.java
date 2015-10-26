package com.cs.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.web.client.RestTemplate;

import com.cs.domain.DataPointSeries;

public class TestAWSRestCall {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();
		final String uri = "http://default-environment-frudkpvgd8.elasticbeanstalk.com/{dataPoint}/{startTime}/{endTime}";

		Map<String, String> params = new HashMap<String, String>();
		params.put("dataPoint", "data point name");
		params.put("startTime", "2015-01-01T02:00:00");
		params.put("endTime", "2015-12-01T02:00:00");

		executeTest(uri, params);

		long end = System.currentTimeMillis();
		long total = end - start;
		System.out.println("Total time(ms) = " + total);

//		Timer timer = new Timer();
//		timer.schedule(new TimerTask() {
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				executeTest(uri, params);
//			}
//		}, 1000, 500);
//
	}

	private static void executeTest(final String uri, Map<String, String> params) {
		RestTemplate restTemplate = new RestTemplate();
		DataPointSeries result = restTemplate.getForObject(uri, DataPointSeries.class, params);

		System.out.println(result.toString());
		System.out.println("Size = " + result.getValues().size());
	}

}
