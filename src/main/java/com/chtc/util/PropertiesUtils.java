package com.chtc.util;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtils {

    private static Properties prop = null;

    static {
        /**
         * 读取根目录下的配置文件
         */
        ResourceLoader loader = new DefaultResourceLoader();
        Resource resource = loader.getResource("classpath:config.properties");
        EncodedResource encRes = new EncodedResource(resource, "UTF-8");
        try {
//            InputStreamReader inputStreamReader = new InputStreamReader(PropertiesUtils.class.getResourceAsStream("classpath:config.properties"), "UTF-8");
            prop = PropertiesLoaderUtils.loadProperties(encRes);
//            prop.load(inputStreamReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据key获取配置文件中的value
     *
     * @param key
     * @return value
     */

    public static String getProperty(String key) {
        String value = null;
        if (prop != null) {
            value = prop.getProperty(key);
        }
        return value;
    }
}
