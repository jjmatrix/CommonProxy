package org.jmatrix.proxy.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author jmatrix
 * @date 16/4/19
 */
public class Configuration {

    private final static Logger LOGGER = LoggerFactory.getLogger(Configuration.class);

    private final static String PLACEHOLDER_PRE = "${";

    private final static String PLACEHOLDER_SUB = "}";

    private static Properties properties = new Properties();

    private static Map<String, String> resolverCache = new HashMap<>();

    static {
        Map<String, String> envProperty = System.getenv();
        envProperty.forEach((key, value) -> {
            if (value.endsWith(".properties")) {
                loadProperties(properties, value);
            } else if (value.startsWith("proxy.")) {
                properties.put(key, value);
            }
        });
        //load proxy.properties
        loadProperties(properties, Configuration.class.getResourceAsStream("/proxy.properties"));
    }

    private static void loadProperties(Properties properties, InputStream inputStream) {
        try {
            properties.load(inputStream);
        } catch (Exception e) {
            LOGGER.error("load properties error", e);
        }
    }

    private static void loadProperties(Properties properties, String filename) {
        try {
            File propertiesFile = new File(filename);
            if (!propertiesFile.exists())
                return;
            properties.load(new FileInputStream(propertiesFile));
        } catch (Exception e) {
            LOGGER.error("load properties error", e);
        }
    }

    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        String property = getProperty(key);
        if (property == null || property.length() == 0)
            return defaultValue;
        return Boolean.valueOf(property);
    }

    public static int getIntProperty(String key, int defaultValue) {
        String property = getProperty(key);
        if (property == null || property.length() == 0)
            return defaultValue;
        try {
            return Integer.parseInt(property);
        } catch (Exception e) {
            LOGGER.error("parse integer property error.", e);
        }
        return defaultValue;
    }

    public static long getLongProperty(String key, long defaultValue) {
        String property = getProperty(key);
        if (property == null || property.length() == 0)
            return defaultValue;
        try {
            return Long.parseLong(property);
        } catch (Exception e) {
            LOGGER.error("parse long property error.", e);
        }
        return defaultValue;
    }

    public static String getProperty(String key) {
        return getProperty(key, null);
    }

    public static String getProperty(String key, String defaultValue) {
        String value = (String) properties.get(key);
        if (value == null) {
            return defaultValue;
        }
        return resolverPlaceHolder(value);
    }


    private static String resolverPlaceHolder(String resolverKey) {
        String resolverValue = resolverCache.get(resolverKey);
        if (resolverValue != null) {
            return resolverValue;
        }

        int fIndex = resolverKey.indexOf(PLACEHOLDER_PRE);
        if (fIndex == -1) {
            return resolverKey;
        }

        StringBuilder builder = new StringBuilder(resolverKey);

        while (fIndex > -1) {
            int lIndex = builder.indexOf(PLACEHOLDER_SUB);

            int start = fIndex + PLACEHOLDER_PRE.length();

            if (fIndex == 0) {
                String varName = builder.substring(start, start + lIndex
                        - PLACEHOLDER_PRE.length());
                builder.replace(fIndex, fIndex + lIndex + 1, getProperty(varName));
            } else {
                String varName = builder.substring(start, lIndex);
                builder.replace(fIndex, lIndex + 1, getProperty(varName));
            }

            fIndex = builder.indexOf(PLACEHOLDER_PRE);
        }

        resolverValue = builder.toString();
        resolverCache.put(resolverKey, resolverValue);

        return resolverValue;
    }
}
