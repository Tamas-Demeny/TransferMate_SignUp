package org.example.support;

import com.mailslurp.apis.InboxControllerApi;
import com.mailslurp.apis.WaitForControllerApi;
import com.mailslurp.clients.ApiClient;
import com.mailslurp.clients.ApiException;
import com.mailslurp.clients.Configuration;
import com.mailslurp.models.Email;
import com.mailslurp.models.InboxDto;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import okhttp3.OkHttpClient;
import org.example.pages.PasswordCreationPage;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@DefaultUrl("https://playground.mailslurp.com")
public class TemporaryMail extends PageObject {

    public TemporaryMail() {
    }
    public static final String MYAPIKEY = System.getenv("MYAPIKEY");
    private static final Long TIMEOUT = 30000L;
    private static final ApiClient MYCLIENT;
    private static InboxDto inbox;
    public static InboxControllerApi inboxControllerApi;

    public ApiClient getMyClient() {
        return MYCLIENT;
    }

    static {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .build();

        MYCLIENT = Configuration.getDefaultApiClient();

        MYCLIENT.setConnectTimeout(TIMEOUT.intValue());
        MYCLIENT.setWriteTimeout(TIMEOUT.intValue());
        MYCLIENT.setReadTimeout(TIMEOUT.intValue());

        MYCLIENT.setHttpClient(httpClient);
        MYCLIENT.setApiKey(MYAPIKEY);

        inboxControllerApi = new InboxControllerApi(MYCLIENT);
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

    OffsetDateTime currentTime = OffsetDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
    WaitForControllerApi waitForControllerApi = new WaitForControllerApi(MYCLIENT);

    public Email getReceivedEmail() throws ApiException {
        return  waitForControllerApi
                .waitForLatestEmail(inbox.getId(),
                        TIMEOUT,
                        false,
                        null,
                        currentTime,
                        null,
                        10000L);
    }

    public String getLink() throws ApiException {
        return Arrays.stream(getReceivedEmail().getBody().split(System.getProperty("line.separator")))
                .filter(l -> l.contains("activate_new_account")).findFirst().get();
    }

    public PasswordCreationPage openActivationLink(String page) {
        return new PasswordCreationPage(page);
    }

}
