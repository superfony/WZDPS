package com.epsmart.wzdp.http.request.model;


import java.io.File;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

/**
 * HTTP Post parameter
 * 
 * @author zhangdq
 */
public class RequestParameter implements java.io.Serializable,
		Comparable<RequestParameter> {
	private static final long serialVersionUID = -8708108746980739212L;

	private String name;
	private String value;
	private File file = null;
	private boolean needUrlencode = true;

	public RequestParameter(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public RequestParameter(String name, double value) {
		this.name = name;
		this.value = String.valueOf(value);
	}

	public RequestParameter(String name, int value) {
		this.name = name;
		this.value = String.valueOf(value);
	}

	public RequestParameter(String name, File file) {
		this.name = name;
		this.file = file;
	}

	public RequestParameter(String name, String value, boolean needUrlencode) {
		this(name, value);
		this.needUrlencode = needUrlencode;
	}

	public RequestParameter(String name, String value, File file,
			boolean needUrlencode) {
		this(name, value, needUrlencode);
		this.file = file;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public File getFile() {
		return file;
	}

	public boolean isFile() {
		return null != file;
	}

	private static final String JPEG = "image/jpeg";
	private static final String GIF = "image/gif";
	private static final String PNG = "image/png";
	private static final String OCTET = "application/octet-stream";

	/**
	 * 
	 * @return content-type
	 */
	public String getContentType() {
		if (!isFile()) {
			throw new IllegalStateException("not a file");
		}
		String contentType;
		String extensions = file.getName();
		int index = extensions.lastIndexOf(".");
		if (-1 == index) {
			// no extension
			contentType = OCTET;
		} else {
			extensions = extensions.substring(extensions.lastIndexOf(".") + 1)
					.toLowerCase(Locale.CHINESE);
			if (extensions.length() == 3) {
				if ("gif".equals(extensions)) {
					contentType = GIF;
				} else if ("png".equals(extensions)) {
					contentType = PNG;
				} else if ("jpg".equals(extensions)) {
					contentType = JPEG;
				} else {
					contentType = OCTET;
				}
			} else if (extensions.length() == 4) {
				if ("jpeg".equals(extensions)) {
					contentType = JPEG;
				} else {
					contentType = OCTET;
				}
			} else {
				contentType = OCTET;
			}
		}
		return contentType;
	}

	public static boolean containsFile(RequestParameter[] params) {
		boolean containsFile = false;
		if (null == params) {
			return false;
		}
		for (RequestParameter param : params) {
			if (param.isFile()) {
				containsFile = true;
				break;
			}
		}
		return containsFile;
	}

	static boolean containsFile(List<RequestParameter> params) {
		boolean containsFile = false;
		for (RequestParameter param : params) {
			if (param.isFile()) {
				containsFile = true;
				break;
			}
		}
		return containsFile;
	}

	public static RequestParameter[] getParameterArray(String name, String value) {
		return new RequestParameter[] { new RequestParameter(name, value) };
	}

	public static RequestParameter[] getParameterArray(String name, int value) {
		return getParameterArray(name, String.valueOf(value));
	}

	public static RequestParameter[] getParameterArray(String name1,
			String value1, String name2, String value2) {
		return new RequestParameter[] { new RequestParameter(name1, value1),
				new RequestParameter(name2, value2) };
	}

	public static RequestParameter[] getParameterArray(String name1,
			int value1, String name2, int value2) {
		return getParameterArray(name1, String.valueOf(value1), name2,
				String.valueOf(value2));
	}

	@Override
	public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + value.hashCode();
		result = 31 * result + (file != null ? file.hashCode() : 0);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (obj instanceof RequestParameter) {
			RequestParameter that = (RequestParameter) obj;

			if (file != null ? !file.equals(that.file) : that.file != null)
				return false;

			return this.name.equals(that.name) && this.value.equals(that.value);
		}
		return false;
	}

	@Override
	public String toString() {
		return "PostParameter{" + "name='" + name + '\'' + ", value='" + value
				+ '\'' + ", file=" + file + '}';
	}

	public int compareTo(RequestParameter param) {
		int compared;
		RequestParameter that = (RequestParameter) param;
		compared = name.compareTo(that.name);
		if (0 == compared) {
			compared = value.compareTo(that.value);
		}
		return compared;
	}

	public static String encodeParameters(RequestParameter[] httpParams) {
		if (null == httpParams) {
			return "";
		}
		StringBuffer buf = new StringBuffer();
		int paramLen = httpParams.length;
		for (int i = 0; i < paramLen; i++) {
			if (httpParams[i].isFile()) {
				throw new IllegalArgumentException("parameter ["
						+ httpParams[i].name + "]should be text");
			}
			if (i != 0) {
				buf.append("&");
			}
			try {
				buf.append(URLEncoder.encode(httpParams[i].name, "UTF-8"))
						.append("=")
						.append(URLEncoder.encode(httpParams[i].value, "UTF-8"));
			} catch (java.io.UnsupportedEncodingException neverHappen) {
			}
		}
		return buf.toString();
	}

	public boolean isNeedUrlencode() {
		return needUrlencode;
	}

	public void setNeedUrlencode(boolean needUrlencode) {
		this.needUrlencode = needUrlencode;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setFile(File file) {
		this.file = file;
	}

}
