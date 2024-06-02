package com.example.Security.service;

import com.example.Security.entities.ConfirmationToken;
import com.example.Security.entities.User;
import com.example.Security.exception.TokenAlreadyConfirmedException;
import com.example.Security.exception.TokenExpiredException;
import com.example.Security.exception.TokenNotFoundException;
import com.example.Security.repositories.ConfirmationTokenRepository;
import com.example.Security.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserRepository userRepository;

    public ConfirmationTokenService(ConfirmationTokenRepository confirmationTokenRepository, UserRepository userRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void saveConfirmationToken(ConfirmationToken token){
        confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token){
        return confirmationTokenRepository.findByToken(token);
    }

    @Transactional
    public String confirmToken(String token){
        ConfirmationToken confirmationToken = this
                .getToken(token)
                .orElseThrow(() -> new TokenNotFoundException(token));
        if(confirmationToken.getConfirmedAt() != null){
            throw new TokenAlreadyConfirmedException(token);
        }
        if(confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new TokenExpiredException(token);
        }
        this.setConfirmedAt(token);
        this.enableUser(confirmationToken.getUser().getEmail());
        return "Confirmed";
    }

    public int setConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }

    public void enableUser(String email){
        User existingUser =  userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException(String.format("confirmed user with email %s does not exist.", email)));
        existingUser.setEnabled(true);
        userRepository.save(existingUser);
    }

    public void deleteTokenByUser(User user){
        this.confirmationTokenRepository.deleteByUser(user);
    }
}
