package main_service.user.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.config.security.JwtAuthenticationResponse;
import main_service.config.security.JwtService;
import main_service.exception.model.BadRequestException;
import main_service.user.dto.SignInRequest;
import main_service.user.dto.SignUpRequest;
import main_service.user.entity.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JavaMailSender mailSender;

    /**
     * Регистрация пользователя
     *
     * @param request данные пользователя
     * @param siteURL
     * @return токен
     */
    public void signUp(SignUpRequest request, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String verificationCode = RandomStringUtils.random(64);
        String generationCode = RandomStringUtils.random(32);

        var user = User.builder()
                .username(request.getUsername().toLowerCase())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(false)
                .subscribed(false)
                .verificationCode(verificationCode)
                .build();

        userService.create(user);

        sendVerificationEmail(user, siteURL);
    }

    private void sendVerificationEmail(User user, String siteURL)
            throws MessagingException, UnsupportedEncodingException {

        String toAddress = user.getEmail();
        String fromAddress = "coverit024@gmail.com"; //TODO make variable
        String senderName = "CoverIt";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "infinitely yours,<br>"
                + "CoverIt.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getUsername());
        String verifyURL = siteURL + "/auth/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);
    }


        /**
         * Аутентификация пользователя
         *
         * @param request данные пользователя
         * @return токен
         */
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        var user = userService
                .getByEmail(request.getEmail());

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));

        if (user.isEnabled()) {
            var jwt = jwtService.generateToken(user);
            log.info("user with id " + user.getId() + " has been signed in successfully");
            return new JwtAuthenticationResponse(jwt);
        } else {
            throw new BadRequestException("user with id " + user.getId() + "is not enabled");
        }
    }
}