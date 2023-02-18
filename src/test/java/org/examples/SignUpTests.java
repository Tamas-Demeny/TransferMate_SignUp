package org.examples;

import com.mailslurp.clients.ApiException;
import com.mailslurp.models.InboxDto;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.junit.CucumberOptions;
import net.thucydides.core.annotations.Steps;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.pages.CheckMailPage;
import org.example.pages.PasswordCreationPage;
import org.example.steps.CheckMailSteps;
import org.example.steps.SignUpSteps;
import org.example.support.SmsReader;
import org.example.support.TemporaryMail;
import org.junit.Assert;
import org.junit.Test;

import javax.script.ScriptException;

public class SignUpTests {
    @Steps
    SignUpSteps signUpPageSteps = new SignUpSteps();
    @Steps
    CheckMailSteps checkMailSteps;
    @Steps
    TemporaryMail temporaryMail = new TemporaryMail();

    public SignUpTests() throws ApiException {
    }

    @Given("User is on sign up page")
    public void openTransferMateWebSite() {
        signUpPageSteps.openWebSite()
                .cookiesBoxCheck();
    }

    @When("User chooses account {} radio button")
    public void userChoosesAccountTypeRadioButton(String accountType) {
        signUpPageSteps.selectAccountType(accountType);
    }

    @And("User selects {} country")
    public void userSelectsCountry(String country) {
        signUpPageSteps.selectCountryField(country);
    }

    @And("User enters {} and {} name")
    public void userEntersFirstAndLastName(String firstName, String lastName) {
        signUpPageSteps.completeFirstNameField(firstName)
                .completeLastNameField(lastName);
    }

    @And("User enters email address")
    public void userEntersEmailAddress() {
        signUpPageSteps.completeEmailField(randomEmail());
    }

    @And("User selects the {} mobile phone prefix")
    public void userSelectsTheCountryPhonePrefix(String country) {
        signUpPageSteps.selectPhonePrefix(country);
    }

    @And("User enters mobile {} number")
    public void userEntersMobilePhoneNumber(String number) {
        signUpPageSteps.completePhoneNumberField(number);
    }

    @And("User ticks ToU and Marketing check-boxes")
    public void userTicksToUAndMarketingCheckBoxes() {
        signUpPageSteps.termsBoxCheck()
                .marketingBoxCheck();
    }

    @And("User fills in captcha")
    public void userFillsInCaptcha() throws ScriptException {
        signUpPageSteps.completeCaptchaField();
    }

    @And("User clicks on Open my free account button")
    public void userClicksOnButton() {
        checkMailSteps = signUpPageSteps.submitApplication();
    }

    @Then("User is redirected to the {} page")
    public void userIsRedirectedToTheEmailAndMobileNumberVerificationPage(String pageTitle) {
        Assert.assertEquals(pageTitle, checkMailSteps.pageTitle());
    }

    private String randomEmail() {
        return RandomStringUtils.randomAlphabetic(10) + "@yahoo.com";
    }

    @Then("User gets invalid First and Last name warning")
    public void userGetsInvalidFirstAndLastNameWarning() {
        Assert.assertEquals("The message was: " + signUpPageSteps.getErrorMessage("first_name"),
                "Please enter correct information!",
                signUpPageSteps.getErrorMessage("first_name"));
        Assert.assertEquals("The message was: " + signUpPageSteps.getErrorMessage("last_name"),
                "Please enter correct information!",
                signUpPageSteps.getErrorMessage("last_name"));

    }

    @And("User enters used email address")
    public void userEntersUsedEmailAddress() {
        signUpPageSteps.completeEmailField("tamas.demeny94@yahoo.com");
    }

    @Then("User gets already used email warning")
    public void userGetsAlreadyUsedEmailWarning() {
        Assert.assertEquals("The message was:" + signUpPageSteps.getErrorMessage("email"),
                "Already exists!",
                signUpPageSteps.getErrorMessage("email"));
    }

    @And("User enters invalid email address")
    public void userEntersInvalidEmailAddress() {
        signUpPageSteps.completeEmailField(randomEmail() + "@gmail.com");
    }

    @Then("User gets incorrect email format warning")
    public void userGetsIncorrectEmailFormatWarning() {
        Assert.assertEquals("The message was: " + signUpPageSteps.getErrorMessage("email"),
                "Please enter correct information!",
                signUpPageSteps.getErrorMessage("email"));
    }

    @Then("User gets invalid phone number warning")
    public void userGetsInvalidPhoneNumberWarning() {
        Assert.assertEquals("The message was: " + signUpPageSteps.getErrorMessage("pin_mobile_number"),
                "Please enter correct information!",
                signUpPageSteps.getErrorMessage("pin_mobile_number"));
    }

    @Then("User gets error messages for mandatory fields")
    public void userGetsErrorMessagesForMandatoryFields() {
        Assert.assertEquals("The number of errors were: " + signUpPageSteps.listOfErrors().size(),
                11,
                signUpPageSteps.listOfErrors().size());
    }

    @Test
    public void print() {
        System.out.println(System.getenv("MYAPIKEY"));
    }

    @Test
    public void t() throws ApiException, ScriptException {

        InboxDto email = temporaryMail.createEmail();

        signUpPageSteps.openWebSite()
                .cookiesBoxCheck()
                .selectAccountType("individual")
                .completeFirstNameField("Lajos")
                .completeLastNameField("Kelemen")
                .completeEmailField(email.getEmailAddress())
                .selectCountryField("Romania")
                .selectPhonePrefix("Romania")
                .completePhoneNumberField("747674589")
                .termsBoxCheck()
                .completeCaptchaField()
                .submitApplication();
        System.out.println(temporaryMail.getReceivedEmail().toString());
        temporaryMail.openActivationLink(temporaryMail.getText());

        SmsReader smsReader = new SmsReader();
        PasswordCreationPage passwordCreationPage = new PasswordCreationPage();
        passwordCreationPage.fillInPasswords()
                                .submitPassword()
                                .fillInPin(smsReader.getPINCodeFromSMS())
                                .submitPin();




    }
}
