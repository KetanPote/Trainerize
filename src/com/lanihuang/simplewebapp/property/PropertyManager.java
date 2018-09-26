package com.lanihuang.simplewebapp.property;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyManager {
    private static PropertyManager instance;

    private Properties properties;
    //private final String propertyFileName = System.getProperty("user.dir")+ "/src/main/java/property/config.properties";
    private final String propertyFileName = System.getProperty("user.dir") + "/config.properties";

    private PropertyManager() {
        properties = new Properties();
        try {
            //  InputStream input = new FileInputStream(propertyFileName);
            //  properties.load(input);
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Properties props = new Properties();
            try (InputStream resourceStream = loader.getResourceAsStream(propertyFileName)) {
                props.load(resourceStream);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PropertyManager getInstance() {
        if (instance == null) {
            instance = new PropertyManager();
        }
        return instance;
    }


    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}

