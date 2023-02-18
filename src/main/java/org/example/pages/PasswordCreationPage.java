package org.example.pages;

public class PasswordCreationPage  extends BasePage{
    public PasswordCreationPage(String link){
        openUrl(link);
    }

    public PasswordCreationPage fillInPasswords() {
        typeInto($("#password"), "NewPassword3");
        typeInto($("#confirm_password"), "NewPassword3");
        return this;
    }

    public PasswordCreationPage submitPassword() {
        clickOn($("#button_subscribe"));
        return this;
    }

    public PasswordCreationPage fillInPin(String pin) {
        typeInto($("#user_pin"), pin);
        return this;
    }

    public HomePage submitPin() {
        clickOn($("#popup-verify-pin-btn"));
        return new HomePage();
    }

}
