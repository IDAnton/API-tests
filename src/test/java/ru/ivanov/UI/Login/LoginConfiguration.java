package ru.ivanov.UI.Login;

import org.aeonbits.owner.Config;

@Config.Sources({"classpath:UI/login.properties"})
public interface LoginConfiguration extends Config {
    @Key("loginUrl")
    String loginUrl();

    @Key("baseUrl")
    String baseUrl();
}
