package com.epsmart.wzdp.http.xml.parser.dom;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.util.Log;

import com.epsmart.wzdp.http.response.model.StatusEntity;
import com.epsmart.wzdp.http.xml.parser.DefaultXmlParser;

public class DefaultDomParser extends DefaultXmlParser {
	protected StatusEntity response;
	protected Document document;
	protected DocumentBuilder builder;

	public DefaultDomParser() {
		// 创建解析器
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	private void init() {
		try {
			document = builder.parse(new InputSource(new ByteArrayInputStream(
					getXml().getBytes("utf-8"))));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (SAXException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void parse() {
		init();
	}

	@Override
	public Object getContent() {
		return response;
	}

}
