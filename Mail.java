package netJava2020MsC_fn26393_project_final;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mail {
	private static final String PROP_PATH = "src/prop.properties";
	
	private static String emailSender;
	private static String emailReceiver;
	private static String content;
	
	
	public static void sendEmail(String to, String from, String emailContent) {
		Mail.setContent(emailContent);
		
		Properties prop = new Properties();
		FileInputStream ip = null;
		try {
			ip = new FileInputStream(PROP_PATH);
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		try {
			prop.load(ip);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		String username = prop.getProperty("username");
        String password = prop.getProperty("password");
		
		prop.put("mail.smtp.host", prop.getProperty("host"));
        prop.put("mail.smtp.port", prop.getProperty("port"));
        prop.put("mail.smtp.auth", prop.getProperty("auth"));
        prop.put("mail.smtp.starttls.enable", prop.getProperty("starttls"));
        
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        Mail.setEmailSender(from);
        Mail.setEmailReceiver(to);
	    Message message = new MimeMessage(session);
	    try {
			message.setFrom(new InternetAddress(Mail.getEmailSender()));
			
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(Mail.getEmailReceiver()));
			
		    message.setSubject("From chat app");
		    
		    message.setText(Mail.getContent());
		    
		    Transport.send(message);
		} catch (MessagingException e) {
			System.out.println(e.getMessage());
		}
	    
	}
	
	public static String getEmailSender() {
		return emailSender;
	}

	public static void setEmailSender(String email) {
		Mail.emailSender = email;
	}
	
	public static String getEmailReceiver() {
		return emailReceiver;
	}

	public static void setEmailReceiver(String email) {
		Mail.emailReceiver = email;
	}
	
	public static String getContent() {
		return content;
	}

	public static void setContent(String content) {
		Mail.content = content;
	}
	
}
