package com.example.Security.service;

import com.example.Security.Email.EmailSender;
import com.example.Security.dto.reset_password.ResetPasswordDTO;
import com.example.Security.entities.PasswordResetToken;
import com.example.Security.entities.User;
import com.example.Security.exception.InvalidTokenException;
import com.example.Security.exception.ResourceNotFoundException;
import com.example.Security.exception.TokenExpiredException;
import com.example.Security.exception.TokenNotFoundException;
import com.example.Security.repositories.PasswordResetRepository;
import com.example.Security.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class PasswordResetService {
    private final PasswordResetRepository passwordResetRepository;
    private UserRepository userRepository;
    private EmailSender emailSender;
    private PasswordEncoder passwordEncoder;
    private Logger LOGGER = LoggerFactory.getLogger(PasswordResetService.class);

    public PasswordResetService(PasswordResetRepository passwordResetRepository, UserRepository userRepository, EmailSender emailSender, PasswordEncoder passwordEncoder) {
        this.passwordResetRepository = passwordResetRepository;
        this.userRepository = userRepository;
        this.emailSender = emailSender;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void createResetToken(String email){
        String token = UUID.randomUUID().toString();
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("user", "email", email));
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .user(existingUser)
                .token(token)
                .expiryDate(LocalDateTime.now(Clock.systemDefaultZone()).plusHours(1))
                .build();
        passwordResetRepository.save(resetToken);
        sendResetPasswordEmail(existingUser.getEmail(), existingUser.getFirstName(), token);
    }

    @Transactional
    public void resetPassword(ResetPasswordDTO resetPasswordDTO){
        if(isTokenValid(resetPasswordDTO.getToken())){
            String email = resetPasswordDTO.getEmail();
            PasswordResetToken resetToken = passwordResetRepository.findByToken(resetPasswordDTO.getToken())
                    .orElseThrow(() -> new ResourceNotFoundException("Reset Token", "token", resetPasswordDTO.getToken()));
            User existingUser = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("user", "email", email));

            existingUser.setPassword(passwordEncoder.encode(resetPasswordDTO.getNewPassword()));
            userRepository.save(existingUser);
            resetToken.setResetAt(LocalDateTime.now());
            passwordResetRepository.save(resetToken);
        } else {
            throw new InvalidTokenException();
        }
    }

    @Transactional
    private boolean isTokenValid(String token){
        PasswordResetToken existingToken = passwordResetRepository.findByToken(token)
                .orElseThrow(() -> new TokenNotFoundException(token));


        if(existingToken.getExpiryDate().isBefore(LocalDateTime.now()) || existingToken.getResetAt() != null){
            throw new TokenExpiredException(token);
        }

        return true;
    }

    private void sendResetPasswordEmail(String email, String firstName, String token){
        String body = buildPasswordResetEmail(firstName, token, email);
        emailSender.sendResetPasswordEmail(email, body);
    }

    private String buildPasswordResetEmail(String firstName, String token, String email){
        return "<!DOCTYPE html>\n" +
                "<html lang=\"es\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Restablecimiento de Contraseña</title>\n" +
                "</head>\n" +
                "<body style=\"font-family: Arial, sans-serif; background-color: #f4f4f4; color: #333;\">\n" +
                "    <div style=\"max-width: 600px; margin: auto; padding: 20px; background-color: #ffffff; border: 1px solid #ddd;\">\n" +
                "        <h2 style=\"color: #333;\">Restablecimiento de Contraseña</h2>\n" +
                "        <p>Hola " + firstName +",</p>\n" +
                "        <p>Recibimos una solicitud para restablecer la contraseña de tu cuenta en <strong>UTN ClassMate</strong>. Si realizaste esta solicitud, haz clic en el enlace a continuación para restablecer tu contraseña:</p>\n" +
                "        <p style=\"text-align: center;\">\n" +
                "            <a href=\"http://localhost:4200/reset-password?token=" + token + "&email="+ email + "\" style=\"display: inline-block; padding: 10px 20px; font-size: 16px; color: #ffffff; background-color: #007bff; text-decoration: none; border-radius: 5px;\">\n" +
                "                Restablecer Contraseña\n" +
                "            </a>\n" +
                "        </p>\n" +
                "        <p>Este enlace es válido por 1 hora. Si no puedes hacer clic en el enlace, copia y pega la siguiente URL en tu navegador:</p>\n" +
                "        <p style=\"word-wrap: break-word;\">\n" +
                "            <a href=\"http://localhost:4200/reset-password?token=" + token + "&email="+ email + "\" style=\"color: #007bff;\">http://localhost:4200/reset-password?token=" + token + "&email=" + email +"</a>\n" +
                "        </p>\n" +
                "        <p>Si no solicitaste este cambio, puedes ignorar este correo electrónico. Tu contraseña actual seguirá siendo segura.</p>\n" +
                "        <p>Gracias por utilizar <strong>UTN ClassMate</strong>.</p>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n";
    }
}
