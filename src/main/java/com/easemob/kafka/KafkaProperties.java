package com.easemob.kafka;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;
import java.util.Properties;

/**
 * @author stliu <stliu@apache.org>
 * @date 4/26/15
 */
@ConfigurationProperties("kafka")
public class KafkaProperties {
    private Properties properties = new Properties();

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    /**
     * spring boot 会把属性名按照句点来分割
     * <p>
     * 例如 a.b.c = "hello world"
     * <p>
     * 那么注入的proeprties 会变成
     * <p>
     * a -->
     * b-->
     * c --> "hello world"
     * <p>
     * 而我们实际想要的properties 是
     * <p>
     * a.b.c --> hello world
     */
    public Properties toFlatProperties() {
        return toFlatProperties(getProperties());
    }

    public Properties toFlatProperties(Properties original) {
        return toFlatProperties(original, null);
    }

    public Properties toFlatProperties(Map original, String parentKey) {
        Properties properties = new Properties();
        for (Object key : original.keySet()) {
            Object value = original.get(key);
            String k = parentKey == null ? key.toString() : parentKey + "." + key.toString();
            if (value instanceof Map) {
                properties.putAll(toFlatProperties((Map) value, k));
            } else {
                properties.put(k, value);
            }
        }
        return properties;
    }
}
