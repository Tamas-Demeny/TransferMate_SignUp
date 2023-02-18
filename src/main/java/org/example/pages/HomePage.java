package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class HomePage extends BasePage{
    WebElement accountProcessing = $("#pages_content_49471");
    public boolean accountProcessing() {
        return accountProcessing.isDisplayed();
    }
}
