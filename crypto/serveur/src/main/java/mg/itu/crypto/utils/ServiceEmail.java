package mg.itu.crypto.utils;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class ServiceEmail {

    private final String smtpServer = "smtp.gmail.com";
    private final int smtpPort = 587;
    private final String smtpUsername = "Zotahiana691@gmail.com";
    private final String smtpPassword = "mnyb jket tdbp anvv"; // À sécuriser !

    public void envoyer(String toEmail, String sujet, String message) {
        // Propriétés de configuration SMTP
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", smtpServer);
        properties.put("mail.smtp.port", String.valueOf(smtpPort));

        // Création de la session avec authentification
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUsername, smtpPassword);
            }
        });

        try {
            // Création du message
            Message emailMessage = new MimeMessage(session);
            emailMessage.setFrom(new InternetAddress(smtpUsername, "Nom de votre entreprise"));
            emailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            emailMessage.setSubject(sujet);
            emailMessage.setText(message);

            // Envoi du message
            Transport.send(emailMessage);
            System.out.println("Email envoyé avec succès à " + toEmail);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de l'email : " + e.getMessage());
        }
    }
}
