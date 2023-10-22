package com.ach.typeson.interlay;

import com.ach.typeson.aplication.ConfigProvider;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.cfg.ConfigFeature;

import java.util.HashMap;
import java.util.Map;

public class ConfigProviderMapImpl implements ConfigProvider {

    private final Map<ConfigFeature, Boolean> configMap = initConfigMap();

    @Override
    public boolean getProperty(ConfigFeature feature) {
        if(configMap.containsKey(feature)){
            return configMap.get(feature);
        }
        return false;
    }

    public ConfigProviderMapImpl addProperty(ConfigFeature feature, boolean value){
        configMap.put(feature, value);
        return this;
    }

    private static Map<ConfigFeature, Boolean> initConfigMap() {
        Map<ConfigFeature, Boolean> configMap = new HashMap<>();
        configMap.put(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        configMap.put(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        return configMap;
    }


}
