package io.ssafy.luckyweeky.common.config;

import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlParser {
    public static List<BeanDefinition> parse(String xmlPath) throws Exception {
        List<BeanDefinition> beanDefinitions = new ArrayList<>();

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        parser.parse(xmlPath, new DefaultHandler() {
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes)
                    throws SAXException {
                if ("bean".equals(qName)) {
                    String id = attributes.getValue("id");
                    String className = attributes.getValue("class");
                    beanDefinitions.add(new BeanDefinition(id, className));
                }
            }
        });

        return beanDefinitions;
    }
}