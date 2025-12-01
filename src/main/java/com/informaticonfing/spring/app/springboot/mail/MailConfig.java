package com.informaticonfing.spring.app.springboot.mail;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Provee un bean de JavaMailSender. Si no hay SMTP configurado (spring.mail.host),
 * registra un sender no-op que imprime los mensajes en consola para desarrollo.
 */
@Configuration
public class MailConfig {

    @Bean
    @ConditionalOnMissingBean(JavaMailSender.class)
    public JavaMailSender javaMailSender(Environment env) {
        String host = env.getProperty("spring.mail.host");
        if (host != null && !host.isBlank()) {
            JavaMailSenderImpl impl = new JavaMailSenderImpl();
            impl.setHost(host);
            impl.setPort(env.getProperty("spring.mail.port", Integer.class, 25));
            impl.setUsername(env.getProperty("spring.mail.username"));
            impl.setPassword(env.getProperty("spring.mail.password"));

            Properties props = impl.getJavaMailProperties();
            props.put("mail.transport.protocol", env.getProperty("spring.mail.protocol", "smtp"));
            props.put("mail.smtp.auth", env.getProperty("spring.mail.properties.mail.smtp.auth", "true"));
            props.put("mail.smtp.starttls.enable", env.getProperty("spring.mail.properties.mail.smtp.starttls.enable", "true"));
            return impl;
        }

        // No SMTP configured: return a No-Op sender that logs to console.
        return new NoOpJavaMailSender();
    }

    static class NoOpJavaMailSender implements JavaMailSender {
        @Override
        public MimeMessage createMimeMessage() {
            return new MimeMessage(Session.getDefaultInstance(new Properties()));
        }

        @Override
        public MimeMessage createMimeMessage(java.io.InputStream contentStream) {
            try {
                return new MimeMessage(Session.getDefaultInstance(new Properties()), contentStream);
            } catch (Exception ex) {
                // fallback: return an empty MimeMessage
                return new MimeMessage(Session.getDefaultInstance(new Properties()));
            }
        }

        @Override
        public void send(MimeMessage mimeMessage) {
            try {
                System.out.println("[MAIL-DEBUG] send MimeMessage -> Subject: " + mimeMessage.getSubject());
            } catch (Exception e) {
                System.out.println("[MAIL-DEBUG] send MimeMessage");
            }
        }

        @Override
        public void send(MimeMessage... mimeMessages) {
            for (MimeMessage m : mimeMessages) send(m);
        }

        @Override
        public void send(SimpleMailMessage simpleMessage) {
            System.out.println("[MAIL-DEBUG] send SimpleMailMessage -> To: " + String.join(",", simpleMessage.getTo()) + " Subject: " + simpleMessage.getSubject() + "\n" + simpleMessage.getText());
        }

        @Override
        public void send(SimpleMailMessage... simpleMessages) {
            for (SimpleMailMessage m : simpleMessages) send(m);
        }
    }
}
