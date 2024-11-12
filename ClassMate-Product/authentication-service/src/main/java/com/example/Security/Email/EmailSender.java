package com.example.Security.Email;

public interface EmailSender {
    void sendConfirmationEmail(String to, String email);
    void sendResetPasswordEmail(String to, String email);

}
