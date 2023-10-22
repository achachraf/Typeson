package io.github.achachraf.typeson.aplication;

import com.fasterxml.jackson.databind.cfg.ConfigFeature;

/**
 * The configuration provider entry for the Typeson Library <br>
 * It's designed to use the {@link ConfigFeature} enum as a key to get the boolean value of the feature
 */
public interface ConfigProvider {

    /**
     * Get the boolean value of the feature
     * @param feature the Jackson config feature feature to get the value of
     * @return the boolean value of the feature
     */
    boolean getProperty(ConfigFeature feature);

}
