package org.example.pages;

import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

@DefaultUrl("https://transfermate.io/en/register.asp?")

public class SignUpPage extends BasePage {
    public SignUpPage() {
    }

    public void openWebSite() {
        open();
    }

    public void clickCOuntryListDropDown() {
        $("#country").click();
    }

    public void enterFirstName(String firstName) {
        typeInto($("#first_name"), firstName);
    }

    public void enterLastName(String lastName) {
        typeInto($("#last_name"), lastName);
    }

    public void enterEmail(String email) {
        typeInto($("#email"), email);
    }

    public void enterPhoneNumber(String number) {
        typeInto($("#__pin_mobile_number_mobile_phone"), number);
    }

    public void selectMobilePrefix(String desiredPrefix) {
        clickOn($("#__pin_mobile_number_international_dialing_code"));
        clickOn(phonePrefix(desiredPrefix));
    }

    public void checkMarketingBox() {
        clickOn($(".check-radio-label[for = 'newsletter_and_privacy_policy_agree']"));
    }

    public void completeCaptchaAnswer(String answer) {
        typeInto($("#__calc_captcha_text"), answer);
    }

    public String captchaQuestion() {
        return $("#cal_captcha_f10_question").getText();
    }

    public WebElement selectAccountType(String type) {
        return $("[for='account_type_" + type + "']");
    }

    public void countryOption(String countryName) {
        clickOn($("//select[@id='country']/option[contains(text(),'" + countryName + "')]"));
    }

    public List<WebElement> errorsList() {
        return getDriver().findElements(By.cssSelector(".err"));
    }

    public String errorText(String field) {
        return getDriver().findElement(By.cssSelector("#register_" + field + "_error")).getText();
    }

    public WebElement phonePrefix(String countryName) {
        return $(By.xpath("//option[contains(text(),'" + countryName + " ')]"));
    }

    public void acceptCookies() {
        if ($("#cookies-box").isDisplayed()) {
            $("#cookies-read-more-link").click();
        }
    }

    public void checkTermsCHeckBoc() {
        clickOn($(".check-radio-label[for = 'terms_of_use_agree']"));
    }

    public void clickOpenAccountButton() {
        clickOn($("#button_subscribe"));
    }
}
