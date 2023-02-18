package org.example.support;

import com.mailslurp.apis.InboxControllerApi;
import com.mailslurp.apis.WaitForControllerApi;
import com.mailslurp.clients.ApiClient;
import com.mailslurp.clients.ApiException;
import com.mailslurp.models.Email;
import com.mailslurp.models.InboxDto;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Arrays;

@DefaultUrl("https://playground.mailslurp.com")
public class TemporaryMail extends PageObject {

    public TemporaryMail() throws ApiException {
        myClient.setApiKey(MYAPIKEY);
        myClient.setConnectTimeout(TIMEOUT_MILLIS.intValue());
    }
//    public static final String MYAPIKEY = System.getenv("MYAPIKEY");
    public static final String MYAPIKEY = "f5ac62dd1ec0349acdd25fd8c9f8124438273ec023bba9127cd56821fd50e6dd";
    private static final Long TIMEOUT_MILLIS = 30000L;
    private static final ApiClient myClient = com.mailslurp.clients.Configuration.getDefaultApiClient();
    private static InboxDto inbox;
    InboxControllerApi inboxControllerApi = new InboxControllerApi(myClient);

    public ApiClient getMyClient() {
        return myClient;
    }

    public InboxDto createEmail() throws ApiException {
        return inbox = inboxControllerApi.createInbox(null,
                                                        Arrays.asList(),
                                                        "null",
                                                        "description_example",
                                                        true,
                                                        false,
                                                        null,
                                                        600000L,
                                                        false,
                                                        String.valueOf(InboxDto.InboxTypeEnum.HTTP_INBOX),
                                                        false,
                                                        false);
    }

    OffsetDateTime offsetDateTime = OffsetDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
    WaitForControllerApi waitForControllerApi = new WaitForControllerApi();

    public Email getReceivedEmail() throws ApiException {
        return  waitForControllerApi
                .waitForLatestEmail(inbox.getId(),
                        TIMEOUT_MILLIS,
                        false,
                        null,
                        offsetDateTime,
                        null,
                        10000L);
    }

    public String getText() throws ApiException {
        return Arrays.stream(getReceivedEmail().getBody().split(System.getProperty("line.separator")))
                .filter(l -> l.contains("E-Mail-Activation")).findFirst().get();
    }

    public void openActivationLink(String page) throws ApiException {
        open(page);
    }

}
