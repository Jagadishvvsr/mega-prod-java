package com.example.megaservice.notify;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final JavaMailSender mail;

    public NotificationService(JavaMailSender mail) {
        this.mail = mail;
    }

    public void sendIncidentCreated(String to, Long incidentId, String title) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("Incident created: #" + incidentId);
        msg.setText("Title: " + title);
        mail.send(msg);
    }
}
