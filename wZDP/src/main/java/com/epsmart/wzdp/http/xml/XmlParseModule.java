package com.epsmart.wzdp.http.xml;

import android.util.Log;

import com.epsmart.wzdp.http.response.model.Response;
import com.epsmart.wzdp.http.xml.handler.DefaultSaxHandler;
import com.epsmart.wzdp.http.xml.parser.BaseXmlParser;
import com.epsmart.wzdp.http.xml.parser.sax.DefaultSaxXmlParser;

// ~ File Information
// ============================================================================

/**
 * @Title: 不具备显示能力的各模块基类,提供模块初始化及数据处理方法.以下是使用说明
 * @Description: <ul>
 *               <li>1.初始化模块中的属性,如method,urlCode,url,actionUrl.</li>
 * 
 *               <li>2.postData()发送数据,执行顺序为
 *               preProcess()数据预处理,executePost()执行发送,接收到返回结果后发送通知.
 *               preProcess()和executePost
 *               ()已提供默认的处理和发送xml数据的实现,如不需要重写,可重写initModule()加入postData()
 *               以实现initModule()之后的自动调用.</li>
 * 
 *               <li>3.handler接收到返回通知后,执行afterProcess()对数据进行后处理
 *               ,dataProcess()的处理过程一般为parseXml()解析xml数据,
 *               baseXmlParser负责实际的解析过程.</li>
 * 
 *               <li>4.重写initXmlParser()以提供parseXml()需要的xml解析器.</li>
 * 
 *               <li>5.parseXml()解析完成后,向外发送通知数据处理完毕
 *               ,客户端可以通过baseXmlParser.getContent()取到解析结果.
 *               </ul>
 * 
 * @version 1.0
 */
public class XmlParseModule implements BaseXmlParser {
	private Response response;

	// xml数据解析器
	private BaseXmlParser baseXmlParser;

	public XmlParseModule(Response response) {
		this.response = response;
	}
	/**
	 * TODO: 解析xml. TODO: 用提供的xml解析器解析数据
	 * @param postXml
	 * 待解析的xml数据
	 */
	public void parseXml() {
		// 初始化解析器
		initXmlParser();
		// 开始解析
		if (null != baseXmlParser) {
			baseXmlParser.parse();
		}
	}
	/**
	 * 初始化xml 数据解析器,复写该方法,已提供解析器,这里默认提供sax解析器.
	 */
	public void initXmlParser() {
		if (null == baseXmlParser) {
			// 默认采用Sax解析
			DefaultSaxXmlParser saxXmlParser = new DefaultSaxXmlParser();
			if (saxXmlParser.getXml() == null
					|| "".equals(saxXmlParser.getXml())) {
				saxXmlParser.setXml(response.getResponseAsString());
			}
			saxXmlParser.setHandler(new DefaultSaxHandler());
			baseXmlParser = saxXmlParser;
		} else {
			if (baseXmlParser.getXml() == null
					|| "".equals(baseXmlParser.getXml())) {
				baseXmlParser.setXml(response.getResponseAsString());
			}
		}
	}

	public BaseXmlParser getBaseXmlParser() {
		return baseXmlParser;
	}

	public void setBaseXmlParser(BaseXmlParser baseXmlParser) {
		this.baseXmlParser = baseXmlParser;
	}

	/*
	 * (non-Javadoc)
	 * @see com.easecom.framework.http.xml.parser.BaseXmlParser#parse()
	 */
	@Override
	public void parse() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.easecom.framework.http.xml.parser.BaseXmlParser#getContent()
	 */
	@Override
	public Object getContent() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.easecom.framework.http.xml.parser.BaseXmlParser#setContent(java.lang
	 * .Object)
	 */
	@Override
	public void setContent(Object content) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.easecom.framework.http.xml.parser.BaseXmlParser#setXml(java.lang.
	 * String)
	 */
	@Override
	public void setXml(String xml) {

	}

	@Override
	public String getXml() {
		return baseXmlParser.getXml();
	}

}
