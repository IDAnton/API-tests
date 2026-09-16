package ru.ivanov.UI.Login;


public class LoginSteps {
    private final LoginPage loginPage;

    LoginSteps(String loginPageUrl) {
        loginPage = new LoginPage(loginPageUrl);
    }

    public LoginSteps loginWithCredentials(String username, String password) {
        loginPage
                .setUsername(username)
                .setPassword(password)
                .clickLoginButton();
        return this;
    }

    public boolean isLogged(String loginText) {
        return loginPage.flashFieldHasText(loginText);
    }
}
