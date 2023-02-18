package org.example.support;

import com.twilio.Twilio;
import com.twilio.base.ResourceSet;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageDeleter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class SmsReader {

    private static final String ACCOUNT_SID = "TwilioAccountSid";
    private static final String AUTH_TOKEN = "TwilioAccountToken";
    private static final String TO_PHONE_NUMBER = "747674589";

    public SmsReader(){
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public String getMessage(){
        return getMessages()
                .filter(m -> m.getDirection().compareTo(Message.Direction.INBOUND) == 0)
                .filter(m -> m.getTo().equals(TO_PHONE_NUMBER))
                .map(Message::getBody)
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    public void deleteMessages(){
        getMessages()
                .filter(m -> m.getDirection().compareTo(Message.Direction.INBOUND) == 0)
                .filter(m -> m.getTo().equals(TO_PHONE_NUMBER))
                .map(Message::getSid)
                .map(sid -> Message.deleter(ACCOUNT_SID, sid))
                .forEach(MessageDeleter::delete);

    }

    private Stream<Message> getMessages(){
        ResourceSet<Message> messages = Message.reader(ACCOUNT_SID).read();
        return StreamSupport.stream(messages.spliterator(), false);
    }

    public String getPINCodeFromSMS() {
        Pattern pattern = Pattern.compile("\\b\\d{4}\\b");
        Matcher matcher = pattern.matcher(getMessage());

        String pinCode = "";
        if (matcher.find())
        {
            pinCode = matcher.group(0);
        }
        return pinCode;

    }
}
