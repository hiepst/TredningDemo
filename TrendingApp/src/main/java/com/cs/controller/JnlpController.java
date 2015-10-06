package com.cs.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.*;

/**
 * Controller that gets data required for downloading a JNLP file via the http
 * request and makes it available to them via a model.
 *
 */
public class JnlpController extends AbstractController {

	protected transient Log logger = LogFactory.getLog(getClass());

	/**
	 * Retrieves data required for the dynamic JNLP files and makes it available
	 * to them via the request.
	 */
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// Get the http session.
		HttpSession httpSession = request.getSession();
		if (logger.isTraceEnabled()) {
			logger.trace(httpSession.getId() + ": Intercepting " + request.getMethod() + " request for "
					+ request.getRequestURL()
					+ (request.getQueryString() == null ? "" : "?" + request.getQueryString()));
			logger.trace("User-Agent: " + request.getHeader("User-Agent"));
		}

		String filename = parseFilename(request);
		if (logger.isDebugEnabled()) {
			logger.debug("Requested JNLP file: " + filename);
		}

		// Create the model.
		Map<String, String> model = new HashMap<String, String>();

		return new ModelAndView(filename, model);
	}

	/**
	 * Parses out the filename from the request URI.
	 * <p>
	 * For instance, if the request URI is
	 * 'http://localhost/launch/test.jsp?hello=what' this method will return
	 * 'test'.
	 *
	 * @param request
	 *            the http servlet request for this controller.
	 * @return the parsed filename.
	 */
	private String parseFilename(HttpServletRequest request) {
		String uri = request.getRequestURI();

		if (uri == null || uri.length() == 0) {
			return null;
		}

		int begin = uri.lastIndexOf('/');
		if (begin == -1) {
			begin = 0;
		} else {
			begin++;
		}
		int end;
		if (uri.contains(";")) {
			end = uri.indexOf(";");
		} else if (uri.contains("?")) {
			end = uri.indexOf("?");
		} else {
			end = uri.length();
		}
		String fileName = uri.substring(begin, end);
		if (fileName.contains(".")) {
			fileName = fileName.substring(0, fileName.lastIndexOf("."));
		}

		// Trim down the filename
		fileName = fileName.trim();

		if (fileName.length() == 0) {
			return null;
		}

		return fileName;
	}
}
