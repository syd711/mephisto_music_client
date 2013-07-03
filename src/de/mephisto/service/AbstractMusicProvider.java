package de.mephisto.service;

import org.apache.commons.configuration.Configuration;


/**
 * Abstract superclass for all music provider
 */
abstract public class AbstractMusicProvider implements IMusicProvider {
    private Configuration configuration;
    private String providerId;

    @Override
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String getProviderId() {
        return providerId;
    }

    @Override
    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    protected Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public String toString() {
        return "Music Provider '" + getProviderId() + "'";
    }
}
