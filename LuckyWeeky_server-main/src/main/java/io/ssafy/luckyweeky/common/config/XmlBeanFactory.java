package io.ssafy.luckyweeky.common.config;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class XmlBeanFactory {
    private static final Map<String, Object> beans = new HashMap<>();

    public XmlBeanFactory(String[] xmlPaths) throws Exception {
        for (String xmlPath : xmlPaths)
            for (BeanDefinition definition : XmlParser.parse(xmlPath))
                registerBean(definition);
    }

    private void registerBean(BeanDefinition definition) throws Exception {
        Constructor<?> constructor = Class.forName(definition.getClassName()).getConstructor();
        Object bean = constructor.newInstance();
        beans.put(definition.getId(), bean);
    }

    public static Object getBean(String id) {
        return beans.get(id);
    }
}