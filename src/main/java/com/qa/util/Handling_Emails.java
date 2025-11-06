package com.qa.util;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class Handling_Emails {
	
	private ConfigReader configReadder;
	Properties prop;
	
	private static final Logger logger = LoggerFactory.getLogger(Handling_Emails.class);

	
	public Handling_Emails() {
		configReadder = new ConfigReader();
		prop = configReadder.init_prop();
		
	}
	

	public void sendemailnotification(String[] to, String url, int responsecode, File scrFile) throws IOException {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(prop.getProperty("email"), prop.getProperty("password"));
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(prop.getProperty("email"))); /* sender */
			InternetAddress[] recipients = new InternetAddress[to.length];
			for (int i = 0; i < to.length; i++) {
				recipients[i] = new InternetAddress(to[i]);
			}
			message.setRecipients(Message.RecipientType.TO, recipients);
			message.setSubject("The URL Not Working / Site Is Down");

			MimeMultipart multipart = new MimeMultipart();
			MimeBodyPart textPart = new MimeBodyPart();
			textPart.setText("The URL " + url + " is down/not working. Response code : " + responsecode);
			multipart.addBodyPart(textPart);

			MimeBodyPart attachment = new MimeBodyPart();
			DataSource source = new FileDataSource(scrFile);
			attachment.setDataHandler(new DataHandler(source));
			attachment.setFileName(scrFile.getName());

			multipart.addBodyPart(attachment);
			message.setContent(multipart);
			Transport.send(message);

			// Transport.send(message);

			logger.info("Email sent to " + to); /* receiver */
		} catch (MessagingException e) {
			logger.warn("Failed to send");
		}
	}

	public void sendemailnotification(String[] to, String subject, String body, File scrFile) throws IOException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(prop.getProperty("email"), prop.getProperty("password"));
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(prop.getProperty("email"))); /* sender */
            InternetAddress[] recipients = new InternetAddress[to.length];
            for (int i = 0; i < to.length; i++) {
                recipients[i] = new InternetAddress(to[i]);
            }
            message.setRecipients(Message.RecipientType.TO, recipients);
            message.setSubject(subject);

            MimeMultipart multipart = new MimeMultipart();
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(body);
            multipart.addBodyPart(textPart);

            if (scrFile != null) {
                MimeBodyPart attachment = new MimeBodyPart();
                DataSource source = new FileDataSource(scrFile);
                attachment.setDataHandler(new DataHandler(source));
                attachment.setFileName(scrFile.getName());
                multipart.addBodyPart(attachment);
            }
            message.setContent(multipart);
            Transport.send(message);

            logger.info("Email sent to " + String.join(", ", to)); /* receiver */
        } catch (MessagingException e) {
            logger.warn("Failed to send");
        }
    }

}