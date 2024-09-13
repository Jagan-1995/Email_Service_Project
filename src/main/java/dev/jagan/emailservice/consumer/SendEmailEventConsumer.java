package dev.jagan.emailservice.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jagan.emailservice.dtos.SendEmailEventDto;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

@Component
public class SendEmailEventConsumer {
    private ObjectMapper objectMapper;

    public SendEmailEventConsumer(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "send_Email", groupId = "emailService")
    public void handleSendEmailEvent(String message) throws JsonProcessingException {


            SendEmailEventDto sendEmailEventDto = objectMapper.readValue(message, SendEmailEventDto.class);

            String to = sendEmailEventDto.getTo();
            String from = sendEmailEventDto.getFrom();
            String body = sendEmailEventDto.getBody();
            String subject = sendEmailEventDto.getSubject();

            System.out.println("Sending Email process started");

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
            props.put("mail.smtp.port", "587"); //TLS Port
            props.put("mail.smtp.auth", "true"); //enable authentication
            props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

            //create Authenticator object to pass in Session.getInstance argument
            Authenticator auth = new Authenticator() {
                //override the getPasswordAuthentication method
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, "lcovmuwkdbirkkzh");
                }
            };
            Session session = Session.getInstance(props, auth);

            EmailUtil.sendEmail(session, to, subject, body);




    }
}
