package com.cs.controller;

import java.time.Instant;

import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.domain.DataPointSeries;

@RestController
public class BaseController {

	private static final String VIEW_LAUNCH = "launch";
	private static int counter = 0;
	private static final String VIEW_INDEX = "index";
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(BaseController.class);

	// @RequestMapping(value = "/", method = RequestMethod.GET)
	// public String welcome(ModelMap model) {
	//
	// model.addAttribute("message", "Welcome");
	// model.addAttribute("counter", ++counter);
	// logger.debug("[welcome] counter : {}", counter);
	//
	// // Spring uses InternalResourceViewResolver and return back index.jsp
	// return VIEW_INDEX;
	//
	// }
	//
	// @RequestMapping(value = "/{name}", method = RequestMethod.GET)
	// public String welcomeName(@PathVariable String name, ModelMap model) {
	//
	// model.addAttribute("message", "Welcome " + name);
	// model.addAttribute("counter", ++counter);
	// logger.debug("[welcomeName] counter : {}", counter);
	// return VIEW_INDEX;
	//
	// }
	//
	// @RequestMapping(value = "/launch", method = RequestMethod.GET)
	// public String launchApp(ModelMap model) {
	//
	// model.addAttribute(VIEW_LAUNCH, "TrendingClient");
	// logger.debug("Lauching app");
	//
	// return VIEW_LAUNCH;
	//
	// }

	@RequestMapping(value = "/{dataPoint}/{startTime}/{endTime}", method = RequestMethod.GET)
	public String getDatapointInRange(@PathVariable String dataPoint, @PathVariable String startTime,
			@PathVariable String endTime, Model model) {
		Instant start = Instant.now();
		logger.debug("Getting data point: " + dataPoint + ", start time: " + startTime + ", end time: " + endTime);

		model.addAttribute("dataPoint", getDataPoint(dataPoint, startTime, endTime));

		Instant end = Instant.now();

		return "jsonTemplate";
	}

	// @RequestMapping(value = "/{dataPoint}/{startTime}/{endTime}", method =
	// RequestMethod.GET)
	// public ResponseEntity<DataPoint> get(@PathVariable String dataPoint,
	// @PathVariable String startTime,
	// @PathVariable String endTime) {
	//
	// return new ResponseEntity<DataPoint>(getDataPoint(dataPoint, startTime,
	// endTime), HttpStatus.OK);
	// }

	private DataPointSeries getDataPoint(String dataPoint, String startTime, String endTime) {
		DataPointSeries dp = new DataPointSeries();
		dp.setName(dataPoint);
		dp.setStartTime(startTime);
		dp.setEndTime(endTime);
		return dp;
	}

}
