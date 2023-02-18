package org.example.steps;

import org.example.pages.CheckMailPage;

public class CheckMailSteps {
    CheckMailPage checkMailPage = new CheckMailPage();

    public String pageTitle() {
        return checkMailPage.getPageTitle();
    }


}
