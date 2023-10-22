package io.github.achachraf.typeson.aplication;

import com.fasterxml.jackson.databind.cfg.ConfigFeature;

public interface ConfigProvider {

    boolean getProperty(ConfigFeature feature);

}
