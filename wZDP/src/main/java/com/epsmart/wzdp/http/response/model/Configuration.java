package com.epsmart.wzdp.http.response.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.AccessControlException;
import java.util.Properties;

public class Configuration {
	private static final String PROP_HEADER = "zhilianzhaopin";
	private static Properties defaultProperty;

	static {
		init();
	}

	static void init() {
		defaultProperty = new Properties();
		defaultProperty.setProperty(PROP_HEADER + ".debug", "true");
		// defaultProperty.setProperty(PROP_HEADER+".clientURL",
		// "http://www.zhaopin.com/-{"+PROP_HEADER+".clientVersion}.xml");
		// defaultProperty.setProperty(PROP_HEADER+".http.userAgent",
		// PROP_HEADER+" http://www.zhaopin.com/ /{"+PROP_HEADER+".clientVersion}");
		defaultProperty.setProperty(PROP_HEADER + ".http.useSSL", "false");
		// defaultProperty.setProperty(PROP_HEADER+".http.proxyHost.fallback",
		// "http.proxyHost");
		// defaultProperty.setProperty(PROP_HEADER+".http.proxyPort.fallback",
		// "http.proxyPort");
		defaultProperty.setProperty(PROP_HEADER + ".http.connectionTimeout",
				"20000");
		defaultProperty
				.setProperty(PROP_HEADER + ".http.readTimeout", "120000");
		// defaultProperty.setProperty(PROP_HEADER+".http.retryCount", "3");
		// defaultProperty.setProperty(PROP_HEADER+".http.retryIntervalSecs",
		// "10");
		defaultProperty.setProperty(PROP_HEADER + ".async.numThreads", "1");
		try {
			Class.forName("dalvik.system.VMRuntime");
			defaultProperty.setProperty(PROP_HEADER + ".dalvik", "true");
		} catch (ClassNotFoundException cnfe) {
			defaultProperty.setProperty(PROP_HEADER + ".dalvik", "false");
		}
		DALVIK = getBoolean(PROP_HEADER + ".dalvik");
//		String t4jProps = PROP_HEADER + ".properties";
//		boolean loaded = loadProperties(defaultProperty, "."
//				+ File.separatorChar + t4jProps)
//				|| loadProperties(
//						defaultProperty,
//						Configuration.class.getResourceAsStream("/WEB-INF/"
//								+ t4jProps))
//				|| loadProperties(defaultProperty,
//						Configuration.class.getResourceAsStream("/" + t4jProps));
	}

	@SuppressWarnings("unused")
	private static boolean loadProperties(Properties props, String path) {
		try {
			File file = new File(path);
			if (file.exists() && file.isFile()) {
				props.load(new FileInputStream(file));
				return true;
			}
		} catch (Exception ignore) {
		}
		return false;
	}

	@SuppressWarnings("unused")
	private static boolean loadProperties(Properties props, InputStream is) {
		try {
			props.load(is);
			return true;
		} catch (Exception ignore) {
		}
		return false;
	}

	private static boolean DALVIK;

	public static boolean isDalvik() {
		return DALVIK;
	}

	public static boolean useSSL() {
		return getBoolean(PROP_HEADER + ".http.useSSL");
	}

	public static String getScheme() {
		return useSSL() ? "https://" : "http://";
	}

	public static String getCilentVersion() {
		return getProperty(PROP_HEADER + ".clientVersion");
	}

	public static String getCilentVersion(String clientVersion) {
		return getProperty(PROP_HEADER + ".clientVersion", clientVersion);
	}

	public static String getProxyHost() {
		return getProperty(PROP_HEADER + ".http.proxyHost");
	}

	public static String getProxyHost(String proxyHost) {
		return getProperty(PROP_HEADER + ".http.proxyHost", proxyHost);
	}

	public static String getProxyUser() {
		return getProperty(PROP_HEADER + ".http.proxyUser");
	}

	public static String getProxyUser(String user) {
		return getProperty(PROP_HEADER + ".http.proxyUser", user);
	}

	public static String getClientURL() {
		return getProperty(PROP_HEADER + ".clientURL");
	}

	public static String getClientURL(String clientURL) {
		return getProperty(PROP_HEADER + ".clientURL", clientURL);
	}

	public static String getProxyPassword() {
		return getProperty(PROP_HEADER + ".http.proxyPassword");
	}

	public static String getProxyPassword(String password) {
		return getProperty(PROP_HEADER + ".http.proxyPassword", password);
	}

	public static int getProxyPort() {
		return getIntProperty(PROP_HEADER + ".http.proxyPort");
	}

	public static int getProxyPort(int port) {
		return getIntProperty(PROP_HEADER + ".http.proxyPort", port);
	}

	public static int getConnectionTimeout() {
		return getIntProperty(PROP_HEADER + ".http.connectionTimeout");
	}

	public static int getConnectionTimeout(int connectionTimeout) {
		return getIntProperty(PROP_HEADER + ".http.connectionTimeout",
				connectionTimeout);
	}

	public static int getReadTimeout() {
		return getIntProperty(PROP_HEADER + ".http.readTimeout");
	}

	public static int getReadTimeout(int readTimeout) {
		return getIntProperty(PROP_HEADER + ".http.readTimeout", readTimeout);
	}

	public static int getRetryCount() {
		return getIntProperty(PROP_HEADER + ".http.retryCount");
	}

	public static int getRetryCount(int retryCount) {
		return getIntProperty(PROP_HEADER + ".http.retryCount", retryCount);
	}

	public static int getRetryIntervalSecs() {
		return getIntProperty(PROP_HEADER + ".http.retryIntervalSecs");
	}

	public static int getRetryIntervalSecs(int retryIntervalSecs) {
		return getIntProperty(PROP_HEADER + ".http.retryIntervalSecs",
				retryIntervalSecs);
	}

	public static String getUser() {
		return getProperty(PROP_HEADER + ".user");
	}

	public static String getUser(String userId) {
		return getProperty(PROP_HEADER + ".user", userId);
	}

	public static String getPassword() {
		return getProperty(PROP_HEADER + ".password");
	}

	public static String getPassword(String password) {
		return getProperty(PROP_HEADER + ".password", password);
	}

	public static String getUserAgent() {
		return getProperty(PROP_HEADER + ".http.userAgent");
	}

	public static String getUserAgent(String userAgent) {
		return getProperty(PROP_HEADER + ".http.userAgent", userAgent);
	}

	public static String getOAuthConsumerKey() {
		return getProperty(PROP_HEADER + ".oauth.consumerKey");
	}

	public static String getOAuthConsumerKey(String consumerKey) {
		return getProperty(PROP_HEADER + ".oauth.consumerKey", consumerKey);
	}

	public static String getOAuthConsumerSecret() {
		return getProperty(PROP_HEADER + ".oauth.consumerSecret");
	}

	public static String getOAuthConsumerSecret(String consumerSecret) {
		return getProperty(PROP_HEADER + ".oauth.consumerSecret",
				consumerSecret);
	}

	public static boolean getBoolean(String name) {
		String value = getProperty(name);
		return Boolean.valueOf(value);
	}

	public static int getIntProperty(String name) {
		String value = getProperty(name);
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException nfe) {
			return -1;
		}
	}

	public static int getIntProperty(String name, int fallbackValue) {
		String value = getProperty(name, String.valueOf(fallbackValue));
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException nfe) {
			return -1;
		}
	}

	public static long getLongProperty(String name) {
		String value = getProperty(name);
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException nfe) {
			return -1;
		}
	}

	public static String getProperty(String name) {
		return getProperty(name, null);
	}

	public static String getProperty(String name, String fallbackValue) {
		String value;
		try {
			value = System.getProperty(name, fallbackValue);
			if (null == value) {
				value = defaultProperty.getProperty(name);
			}
			if (null == value) {
				String fallback = defaultProperty.getProperty(name
						+ ".fallback");
				if (null != fallback) {
					value = System.getProperty(fallback);
				}
			}
		} catch (AccessControlException ace) {
			// Unsigned applet cannot access System properties
			value = fallbackValue;
		}
		return replace(value);
	}

	private static String replace(String value) {
		if (null == value) {
			return value;
		}
		String newValue = value;
		int openBrace = 0;
		if (-1 != (openBrace = value.indexOf("{", openBrace))) {
			int closeBrace = value.indexOf("}", openBrace);
			if (closeBrace > (openBrace + 1)) {
				String name = value.substring(openBrace + 1, closeBrace);
				if (name.length() > 0) {
					newValue = value.substring(0, openBrace)
							+ getProperty(name)
							+ value.substring(closeBrace + 1);

				}
			}
		}
		if (newValue.equals(value)) {
			return value;
		} else {
			return replace(newValue);
		}
	}

	public static int getNumberOfAsyncThreads() {
		return getIntProperty(PROP_HEADER + ".async.numThreads");
	}

	public static boolean getDebug() {
		return getBoolean(PROP_HEADER + ".debug");

	}
}
