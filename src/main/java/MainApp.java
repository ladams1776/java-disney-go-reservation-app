import Authentication.SmtpAuthenticator;
import config.UserCredentialConfig;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

public class MainApp {


    public static void main(String[] args) {
        String subject = "testing now";
        String body = "testing body now";
        sendFromGMail(UserCredentialConfig.USERNAME, UserCredentialConfig.PASSWORD, UserCredentialConfig.RECIPIENT, subject, body);
    }

    private static void sendFromGMail(String from, String pass, String to, String subject, String body) {
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        SmtpAuthenticator authenticator = new SmtpAuthenticator(from, pass);
        Session session = Session.getDefaultInstance(props, authenticator);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = new InternetAddress[1];

            toAddress[0] = new InternetAddress(to);

            message.addRecipient(Message.RecipientType.TO, toAddress[0]);

            message.setSubject(subject);
            message.setText(body);
            Transport transport = session.getTransport("smtp");
            transport.connect();
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

        } catch (AddressException ae) {
            ae.printStackTrace();
        } catch (MessagingException me) {
            me.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("worked?");
    }




}
