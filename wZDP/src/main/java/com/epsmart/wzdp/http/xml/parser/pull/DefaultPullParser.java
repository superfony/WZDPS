package com.epsmart.wzdp.http.xml.parser.pull;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.epsmart.wzdp.http.xml.parser.DefaultXmlParser;

public class DefaultPullParser extends DefaultXmlParser {

	@Override
	public void parse() {
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(
					new ByteArrayInputStream(getXml().getBytes("UTF-8")),
					"UTF-8");
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:// 文档开始事件

					break;
				case XmlPullParser.START_TAG:// 标签元素开始
					if ("".equals(parser.getName())) {
						parser.nextText();
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				default:
					break;
				}
				// 下一个元素
				parser.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
