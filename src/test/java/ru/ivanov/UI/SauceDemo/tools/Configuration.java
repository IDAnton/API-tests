package ru.ivanov.UI.SauceDemo.tools;

import org.aeonbits.owner.Config;

@Config.Sources({"classpath:UI/SauceDemo.properties"})
public interface Configuration extends Config {
    @Key("url")
    String url();

    @Key("username")
    String username();

    @Key("password")
    String password();
}
