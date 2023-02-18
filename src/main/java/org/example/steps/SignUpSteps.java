package org.example.steps;

import org.example.pages.CheckMailPage;
import org.example.pages.SignUpPage;
import org.openqa.selenium.WebElement;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.List;

public class SignUpSteps {
    SignUpPage signUpPage = new SignUpPage();

    public SignUpSteps openWebSite() {
        signUpPage.openWebSite();
        return this;
    }

    public SignUpSteps cookiesBoxCheck() {
        signUpPage.acceptCookies();
        return this;
    }

    public SignUpSteps selectCountryField(String countryName) {
        signUpPage.clickCOuntryListDropDown();
        signUpPage.countryOption(countryName);
        return this;
    }

    public SignUpSteps selectPhonePrefix(String countryName) {
        signUpPage.selectMobilePrefix(countryName);
        return this;
    }

    public SignUpSteps completeFirstNameField(String firstName) {
        signUpPage.enterFirstName(firstName);
        return this;
    }

    public SignUpSteps completeLastNameField(String lastName) {
        signUpPage.enterLastName(lastName);
        return this;
    }

    public SignUpSteps completeEmailField(String email) {
        signUpPage.enterEmail(email);
        return this;
    }

    public SignUpSteps completePhoneNumberField(String phoneNumber) {
        signUpPage.enterPhoneNumber(phoneNumber);
        return this;
    }

    public SignUpSteps selectAccountType(String type) {
        signUpPage.selectAccountType(type).click();
        return this;
    }

    public SignUpSteps completeCaptchaField() throws ScriptException {

        signUpPage.completeCaptchaAnswer(String.valueOf(resolveCaptchaText()));
        return this;
    }

    public SignUpSteps termsBoxCheck() {
        signUpPage.checkTermsCHeckBoc();
        return this;
    }

    public SignUpSteps marketingBoxCheck() {
        signUpPage.checkMarketingBox();
        return this;
    }

    public String getErrorMessage(String field) {
        return signUpPage.errorText(field);
    }

    public List<WebElement> listOfErrors() {
        return signUpPage.errorsList();
    }

    public String resolveCaptchaText() throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        String captchaQuestion = signUpPage.captchaQuestion().replace("=", "").trim();

        String answer = engine.eval(captchaQuestion).toString();
        answer = checkAnswer(answer);
        return answer;
    }

    public String checkAnswer(String value) {
        if (value.substring(0, 1).equalsIgnoreCase("-")) {
            value = value.substring(1);
        }
        return value;
    }

    public CheckMailSteps submitApplication() {
        signUpPage.clickOpenAccountButton();
        return new CheckMailSteps();
    }
}
