package org.example.support;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.lang.RandomStringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class NadaEMailService {

    private static final String NADA_EMAIL_DOMAIN = "@nada.ltd";
    private static final String INBOX_MESSAGE_KEY_NAME = "msgs";
    private static final String EMAIL_ID_ROUTE_PARAM = "email-id";
    private static final String MESSAGE_ID_ROUTE_PARAM = "message-id";
    private static final String NADA_EMAIL_INBOX_API = "https://getnada.com/api/v1/inboxes/{email-id}";
    private static final String NADA_EMAIL_MESSAGE_API = "https://getnada.com/api/v1/messages/{message-id}";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final int EMAIL_CHARS_LENGTH = 10;

    private String emailId;


    private void generateEmailId(){
        this.emailId = RandomStringUtils.randomAlphanumeric(EMAIL_CHARS_LENGTH).toLowerCase().concat(NADA_EMAIL_DOMAIN);
    }

    //generates a random email for the first time.
    //call reset for a new random email
    public String getEmailId(){
        if(Objects.isNull(this.emailId)){
            this.generateEmailId();
        }
        return this.emailId;
    }

    //to re-generate a new random email id
    public void reset(){
        this.emailId = null;
    }

    public List<InboxEmail> getInbox() throws UnirestException, IOException {
        String msgs = Unirest.get(NADA_EMAIL_INBOX_API)
                .routeParam(EMAIL_ID_ROUTE_PARAM, this.getEmailId())
                .asJson()
                .getBody()
                .getObject()
                .getJSONArray(INBOX_MESSAGE_KEY_NAME)
                .toString();
        return MAPPER.readValue(msgs, new TypeReference<List<InboxEmail>>() {});
    }

    public EmailMessage getMessageById(final String messageId) throws UnirestException, IOException {
        String msgs = Unirest.get(NADA_EMAIL_MESSAGE_API)
                .routeParam(MESSAGE_ID_ROUTE_PARAM, messageId)
                .asJson()
                .getBody()
                .getObject()
                .toString();
        return MAPPER.readValue(msgs, EmailMessage.class);
    }

    public String getMessageWithSubjectStartsWith() throws IOException, UnirestException {
        return  this.getInbox()
                .stream()
                .filter(ie -> ie.getSubject().contains("E-Mail-Activation"))
                .findFirst()
                .map(InboxEmail::getMessageId)
                .orElseThrow(IllegalArgumentException::new);
    }

}