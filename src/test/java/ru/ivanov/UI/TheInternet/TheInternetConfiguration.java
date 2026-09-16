package ru.ivanov.UI.TheInternet;

import org.aeonbits.owner.Config;

@Config.Sources({"classpath:UI/login.properties"})
public interface TheInternetConfiguration extends Config {
    @Key("loadingUrl")
    String loadingUrl();

    @Key("loginUrl")
    String loginUrl();

    @Key("baseUrl")
    String baseUrl();
}
