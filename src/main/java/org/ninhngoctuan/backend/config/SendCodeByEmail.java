package org.ninhngoctuan.backend.config;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
public class SendCodeByEmail {

    public static Boolean sendEmail(String to, String subject, String body) throws MessagingException {
        // Cài đặt thông số cho việc gửi email
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Tạo đối tượng Session
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // Cung cấp tên người dùng và mật khẩu cho quá trình xác thực
                return new PasswordAuthentication("ninhngoctuan14122003@gmail.com", "ccmt nsje gsqq gzzq");
            }
        });

        // Tạo một đối tượng MimeMessage
        Message message = new MimeMessage(session);

        // Đặt thông tin người gửi
        message.setFrom(new InternetAddress("ninhngoctuan14122003@gmail.com"));

        // Đặt thông tin người nhận
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(to)
        );

        // Đặt tiêu đề của email
        message.setSubject(subject);

        // Đặt nội dung của email
        message.setText(body);

        // Gửi email
        Transport.send(message);

        return true;
    }

    public static void main(String[] args) {
        try {
            String to = "ninhngoctuan01258@gmail.com";
            String subject = "test gmnail";
            String body = "1412233";
            Boolean test = sendEmail(to,subject,body);
            if (test == true){
                System.out.println(" Thanh COng");
            }else {
                System.err.println(" loi");
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}