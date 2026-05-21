package com.huaducdat.storemanagerclient.shared.config;

public class ConfigService {

    private volatile AppConfig appConfig =
            AppConfig.builder().build();

    public AppConfig getConfig() {

        return appConfig;
    }

    public void setConfig(
            AppConfig appConfig
    ) {

        this.appConfig = appConfig;
    }

    public String getApiBaseUrl() {

        return appConfig.getApiBaseUrl();
    }

    public void setApiBaseUrl(
            String apiBaseUrl
    ) {

        appConfig.setApiBaseUrl(apiBaseUrl);
    }

    public int getConnectTimeoutMillis() {

        return appConfig.getConnectTimeoutMillis();
    }

    public int getReadTimeoutMillis() {

        return appConfig.getReadTimeoutMillis();
    }
}
