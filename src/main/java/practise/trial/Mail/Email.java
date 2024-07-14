package practise.trial.Mail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Objects;
import java.util.Properties;

@Service
public class Email {

    @Autowired
    Environment env;

    private static final Logger LOGGER = LogManager.getLogger(Email.class);

    public void sendTo(String mailTo, String subject, Multipart mailContent) throws Exception {
        InternetAddress[] mail = InternetAddress.parse(mailTo, true);
        Properties properties = new Properties();
        properties.put("mail.smtp.host", env.getProperty("spring.mail.host"));
        properties.put("mail.smtp.port", env.getProperty("spring.mail.port"));
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(env.getProperty("spring.mail.username"),
                        env.getProperty("spring.mail.password"));
            }
        };

        Session session = Session.getInstance(properties, auth);

        // creates a new e-mail message
        MimeMessage msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(Objects.requireNonNull(env.getProperty("spring.mail.username"))));
        msg.setRecipients(Message.RecipientType.TO, mail);
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        msg.setContent(mailContent);

        Transport.send(msg);
        LOGGER.info("Mail sent successfully to users: {}", mailTo);
    }
}
