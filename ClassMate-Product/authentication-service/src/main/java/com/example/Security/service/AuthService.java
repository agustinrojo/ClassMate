package com.example.Security.service;

import com.example.Security.Email.EmailSender;
import com.example.Security.dto.auth.AuthReq;
import com.example.Security.dto.auth.AuthenticationResp;
import com.example.Security.dto.register.RegisterReq;
import com.example.Security.dto.register.RegisterRespDTO;
import com.example.Security.dto.token.TokenValidationRequest;
import com.example.Security.dto.token.TokenValidationResponse;
import com.example.Security.dto.user.UserDTO;
import com.example.Security.dto.token.UserTokenValidationRequest;
import com.example.Security.entities.*;
import com.example.Security.exception.EmailAlreadyTakenException;
import com.example.Security.exception.EmailNotValidException;
import com.example.Security.exception.InvalidTokenException;
import com.example.Security.exception.ResourceWithNumericValueDoesNotExistException;
import com.example.Security.repositories.JWTTokenrepository;
import com.example.Security.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service

public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;
    private final JWTTokenrepository jwtTokenRepository;



    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager,
                       EmailValidator emailValidator,
                       ConfirmationTokenService confirmationTokenService,
                       EmailSender emailSender,
                       JWTTokenrepository jwtTokenrepository
                       ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.emailValidator = emailValidator;
        this.confirmationTokenService = confirmationTokenService;
        this.emailSender = emailSender;
        this.jwtTokenRepository = jwtTokenrepository;

    }

    public RegisterRespDTO register(RegisterReq req){
        //check email
        boolean isValidEmail = emailValidator.test(req.getEmail());
        if(!isValidEmail){
            throw new EmailNotValidException(req.getEmail());
        }
        System.out.println(req);
        User user = this.mapToUser(req);


        String token = signUpUser(user);
        String link = "http://localhost:8080/api/auth/confirm?token=" + token;
        emailSender.sendConfirmationEmail(req.getEmail(), buildConfirmationEmail(link, "UTN Classmate", user.getFirstName()));
        return RegisterRespDTO.builder()
                .success(true)
                .build();
    }

    public AuthenticationResp authenticate(AuthReq req){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getEmail(),
                        req.getPassword()
                )
        );
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with email '%s' not found.", req.getEmail())));

        UserDTO userDTO = mapToUserDTO(user);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        //chequear que no exista ningun token activo para el usuario dado.
        //si llegase a existir alguno, se lo debe invalidar o borrar
        revokeAllTokensByUser(user);


        //guardar token generado
        JWTToken token = JWTToken.builder()
                .token(accessToken)
                .loggedOut(false)
                .user(user)
                .build();
        jwtTokenRepository.save(token);

        return mapToAuthenticationResponse(accessToken, refreshToken, userDTO);
    }

    public TokenValidationResponse isUserTokenValid(UserTokenValidationRequest req){
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new ResourceWithNumericValueDoesNotExistException("User", "id", req.getUserId()));
        boolean valid = jwtService.isUserTokenValid(req.getToken(), user);
        if(!valid){
            throw new InvalidTokenException();
        }
        return mapToTokenValidationResponse(valid);
    }

    public TokenValidationResponse validateToken(TokenValidationRequest req){
        boolean valid = jwtService.validateToken(req.getToken());
        if(!valid){
            throw new InvalidTokenException();
        }
        return mapToTokenValidationResponse(valid);
    }

    private void revokeAllTokensByUser(User user) {
        List<JWTToken> validTokenListByUser = jwtTokenRepository.findAllTokensByUser(user.getId());
        if(!validTokenListByUser.isEmpty()){
            validTokenListByUser.forEach(token -> {
                token.setLoggedOut(true);
            });

            jwtTokenRepository.saveAll(validTokenListByUser);
        }
    }



    public AuthenticationResp mapToAuthenticationResponse(String accessToken,String refreshToken,UserDTO userDTO){
        return AuthenticationResp.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userDTO)
                .build();
    }

    public String signUpUser(User user){
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        boolean userExists = existingUser.isPresent();

        //Si el user existe y esta confirmado por mail, tirar una excepcion.
        //Si existe y nunca fue confirmado, borrar el existente
        if(userExists){
            if(existingUser.get().isEnabled()){
                throw new EmailAlreadyTakenException(user.getEmail());
            }else {
                confirmationTokenService.deleteTokenByUser(existingUser.get());
                userRepository.delete(existingUser.get());
            }

        }

        userRepository.save(user);
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                user,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15)
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        //TODO: Send email
        return token;
    }


    public ResponseEntity refreshToken(HttpServletRequest request, HttpServletResponse response) {
        //extract the token form auth header
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        //extract email from token
        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token);

        //check if user exist en db
        User user =  userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("No user found with email %s", email)));

        //check if refreshtoken is valid
        if(jwtService.isRefreshTokenValid(token, user)){
            //generate new access token
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            revokeAllTokensByUser(user);


            //guardar token generado
            JWTToken jwtToken = JWTToken.builder()
                    .token(accessToken)
                    .loggedOut(false)
                    .user(user)
                    .build();
            jwtTokenRepository.save(jwtToken);


            return new ResponseEntity(new AuthenticationResp(accessToken, refreshToken, mapToUserDTO(user)), HttpStatus.OK );
        }

        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }


    public static String buildConfirmationEmail(String confirmationUrl, String siteName, String firstName) {
        return "<!DOCTYPE html>"
                + "<html lang=\"es\">"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                + "<title>Confirmación de Cuenta</title>"
                + "<style>"
                + "body {font-family: Arial, sans-serif; background-color: #f4f4f4; color: #333; padding: 0; margin: 0;}"
                + ".container {width: 100%; max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); overflow: hidden;}"
                + ".header {background-color: #007BFF; color: #ffffff; text-align: center; padding: 20px 0;}"
                + ".header h1 {margin: 0;}"
                + ".content {padding: 20px;}"
                + ".content p {font-size: 16px; line-height: 1.5;}"
                + ".button {display: block; width: 200px; margin: 20px auto; padding: 10px 0; background-color: #007BFF; color: #ffffff; text-align: center; text-decoration: none; border-radius: 5px; font-size: 16px;}"
                + ".footer {text-align: center; padding: 10px 0; background-color: #f4f4f4; color: #777;}"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class=\"container\">"
                + "<div class=\"header\"><h1>Confirmación de Cuenta</h1></div>"
                + "<div class=\"content\">"
                + "<p>Hola,</p>"
                + "<p>Hola "+ firstName + "!  Gracias por registrarte en nuestro sitio. Para completar el registro, por favor confirma tu cuenta haciendo clic en el botón de abajo.</p>"
                + "<a href=\"" + confirmationUrl + "\" class=\"button\">Confirmar Cuenta</a>"
                + "<p>Si el botón anterior no funciona, copia y pega el siguiente enlace en tu navegador:</p>"
                + "<p><a href=\"" + confirmationUrl + "\">" + confirmationUrl + "</a></p>"
                + "<p>Gracias,<br>El equipo de " + siteName + "</p>"
                + "</div>"
                + "<div class=\"footer\"><p>&copy; 2024 " + siteName + ". Todos los derechos reservados.</p></div>"
                + "</div>"
                + "</body>"
                + "</html>";
    }

    public User mapToUser(RegisterReq req){
        return User.builder()
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .carrera(req.getCarrera())
                .legajo(req.getLegajo())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(Role.USER)
                .forumsSubscribed(new ArrayList<>())
                .forumsCreated(new ArrayList<>())
                .build();
    }

    public UserDTO mapToUserDTO(User user){
        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .legajo(user.getLegajo())
                .carrera(user.getCarrera())
                .forumsSubscribed(user.getForumsSubscribed())
                .forumsCreated(user.getForumsCreated())
                .forumsAdmin(user.getForumsAdmin())
                .chatroomIdsIn(user.getChatroomIdsIn())
                .isSynced(user.isGoogleSynced())
                .build();
    }

    public TokenValidationResponse mapToTokenValidationResponse(boolean valid){
        return TokenValidationResponse.builder()
                .valid(valid)
                .build();
    }



}
