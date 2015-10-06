package com.cs.framework;
/*
 * Originated From @(#)JnlpDownloadServlet.java 1.12 10/03/24
 * 
 * Copyright (c) 2006, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * -Redistribution of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *
 * -Redistribution in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 *
 * Neither the name of Oracle or the names of contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MICROSYSTEMS, INC. ("SUN")
 * AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of any
 * nuclear facility.
 */

import java.io.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This Servlet class is an implementation of JNLP Specification's Download
 * Protocols.
 * 
 * All requests to this servlet is in the form of HTTP GET commands. The
 * parameters that are needed are:
 * <ul>
 * <li><code>arch</code>,
 * <li><code>os</code>,
 * <li><code>locale</code>,
 * <li><code>version-id</code> or <code>platform-version-id</code>,
 * <li><code>current-version-id</code>,
 * <li>code>known-platforms</code>
 * </ul>
 * <p>
 * 
 * @version 1.8 01/23/03
 */
@SuppressWarnings("serial")
public class JnlpDownloadServlet extends HttpServlet {
	private static final Log logger = LogFactory.getLog(JnlpDownloadServlet.class);

	private static final String PARAM_JNLP_EXTENSION = "jnlp-extension";

	private static final String PARAM_JAR_EXTENSION = "jar-extension";

	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		// Setup logging
		if (logger.isDebugEnabled()) {
			logger.debug("Initializing Servlet " + getClass().getSimpleName());
		}
		// Get extension from Servlet configuration, or use default
		JnlpResource.setDefaultExtensions(config.getInitParameter(PARAM_JNLP_EXTENSION),
				config.getInitParameter(PARAM_JAR_EXTENSION));
	}

	@Override
	public long getLastModified(HttpServletRequest req) {
		// long buildNumber = BuildNumber.getBuildNumber();
		// TODO Hardcode for now. Should get from a properties or resource file.
		long buildNumber = 20151006;
		if (buildNumber >= 0) {
			return buildNumber;
		} else {
			return super.getLastModified(req);
		}
	}

	public void doHead(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleRequest(request, response, true);
	}

	/**
	 * We handle get requests too - eventhough the spec. only requeres POST
	 * requests
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleRequest(request, response, false);
	}

	private void handleRequest(HttpServletRequest request, HttpServletResponse response, boolean isHead)
			throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("handleRequestInternal( request? " + request.getClass() + ", response? " + response.getClass()
					+ ", isHead? " + isHead);
		}

		String requestStr = request.getRequestURI();
		if (request.getQueryString() != null)
			requestStr += "?" + request.getQueryString().trim();

		// Parse HTTP request
		DownloadRequest dreq = new DownloadRequest(getServletContext(), request);
		if (logger.isInfoEnabled()) {
			logger.info("Request? " + requestStr);
			logger.info("User-Agent? " + request.getHeader("User-Agent"));
		}
		if (logger.isDebugEnabled()) {
			logger.debug("DownloadRequest? " + dreq.toString());
		}

		// long ifModifiedSince = request.getDateHeader("If-Modified-Since");
		try {
			// Decide what resource to return
			JnlpResource jnlpres = locateResource(dreq);

			if (logger.isDebugEnabled()) {
				logger.debug("JnlpResource? " + jnlpres);
			}

			if (logger.isInfoEnabled()) {
				logger.info("Resource returned? " + jnlpres.getPath());
			}

			DownloadResponse dres = null;
			if (isHead) {

				int cl = jnlpres.getResource().openConnection().getContentLength();

				// head request response
				dres = DownloadResponse.getHeadRequestResponse(jnlpres.getMimeType(), jnlpres.getVersionId(),
						jnlpres.getLastModified(), cl);

			}
			// else if (ifModifiedSince != -1 &&
			// (ifModifiedSince / 1000) >=
			// (jnlpres.getLastModified() / 1000)) {
			// // We divide the value returned by getLastModified here by 1000
			// // because if protocol is HTTP, last 3 digits will always be
			// // zero. However, if protocol is JNDI, that's not the case.
			// // so we divide the value by 1000 to remove the last 3 digits
			// // before comparison
			//
			// // return 304 not modified if possible
			// if ( logger.isDebugEnabled() ) {
			// logger.debug("return 304 Not modified");
			// }
			//
			// dres = DownloadResponse.getNotModifiedResponse();
			//
			// }
			else {

				// Return selected resource
				dres = constructResponse(jnlpres, dreq);
			}

			dres.sendRespond(response);

		} catch (ErrorResponseException ere) {
			if (logger.isInfoEnabled()) {
				logger.info("ErrorResponse[" + requestStr + "]? " + ere.toString());
			}

			if (logger.isTraceEnabled()) {
				logger.trace("ErrorResponse[" + requestStr + "]? ", ere);
			}
			// Return response from exception
			ere.getDownloadResponse().sendRespond(response);
		} catch (Throwable e) {
			if (logger.isErrorEnabled()) {
				logger.error("Internal error[" + requestStr + "]? ", e);
			}
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Interprets the download request and convert it into a resource that is
	 * part of the Web Archive.
	 */
	private JnlpResource locateResource(DownloadRequest dreq) throws IOException, ErrorResponseException {
		if (dreq.getVersion() == null) {
			return handleBasicDownload(dreq);
		} else {
			return handleVersionRequest(dreq);
		}
	}

	/**
	 * Given a DownloadPath and a DownloadRequest, it constructs the data stream
	 * to return to the requester
	 */
	private DownloadResponse constructResponse(JnlpResource jnlpres, DownloadRequest dreq) throws IOException {
		// check and see if we can use pack resource
		JnlpResource jr = new JnlpResource(getServletContext(), jnlpres.getName(), jnlpres.getVersionId(),
				jnlpres.getOSList(), jnlpres.getArchList(), jnlpres.getLocaleList(), jnlpres.getPath(),
				jnlpres.getReturnVersionId(), dreq.getEncoding());

		if (logger.isDebugEnabled()) {
			logger.debug("Real resource returned: " + jr);
		}

		// Return WAR file resource
		return DownloadResponse.getFileDownloadResponse(jr.getResource(), jr.getMimeType(), jr.getLastModified(),
				jr.getReturnVersionId());
	}

	private JnlpResource handleBasicDownload(DownloadRequest dreq) throws ErrorResponseException, IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("Executing Basic Protocol lookup");
		}

		// Do not return directory names for basic protocol
		if (dreq.getPath() == null || dreq.getPath().endsWith("/")) {
			throw new ErrorResponseException(DownloadResponse.getNoContentResponse());
		}
		// Lookup resource
		JnlpResource jnlpres = new JnlpResource(getServletContext(), dreq.getPath());
		if (!jnlpres.exists()) {
			throw new ErrorResponseException(DownloadResponse.getNoContentResponse());
		}
		return jnlpres;
	}

	private JnlpResource handleVersionRequest(DownloadRequest dreq) throws IOException, ErrorResponseException {
		if (logger.isDebugEnabled()) {
			logger.debug("Executing Version-based/Extension based lookup");
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Executing Basic Protocol lookup");
		}

		// Do not return directory names for basic protocol
		if (dreq.getPath() == null || dreq.getPath().endsWith("/")) {
			throw new ErrorResponseException(DownloadResponse.getNoContentResponse());
		}
		JnlpResource jnlpres = new JnlpResource(getServletContext(), null, dreq.getCurrentVersionId(), dreq.getOS(),
				dreq.getArch(), dreq.getLocale(), dreq.getPath(), dreq.getVersion(), dreq.getEncoding());
		// Lookup resource
		if (!jnlpres.exists()) {
			throw new ErrorResponseException(DownloadResponse.getNoContentResponse());
		}
		return jnlpres;
	}
}
