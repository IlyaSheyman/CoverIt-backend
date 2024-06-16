package main_service.user.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.config.security.JwtAuthenticationResponse;
import main_service.config.security.JwtService;
import main_service.exception.model.BadRequestException;
import main_service.logs.service.TelegramLogsService;
import main_service.user.dto.SignInRequest;
import main_service.user.dto.SignUpRequest;
import main_service.user.entity.User;
import main_service.user.mapper.UserMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
    @Value("${email.verify.base.url}")
    private String siteURL;
    @Value("${spring.mail.username}")
    private String email;
    private final TelegramLogsService logsService;
    private final UserMapper mapper;


    /**
     * User registration
     *
     * @param request user's data
     */
    public void signUp(SignUpRequest request)
            throws MessagingException, UnsupportedEncodingException {
        String verificationCode = RandomStringUtils.random(64);

        var user = User.builder()
                .username(request.getUsername().toLowerCase())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(false)
                .subscribed(false)
                .verificationCode(verificationCode)
                .build();

        userService.create(user);

        sendVerificationEmail(user);
    }

    private void sendVerificationEmail(User user)
            throws MessagingException, UnsupportedEncodingException {

        String toAddress = user.getEmail();
        String fromAddress = email;
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
        String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);
    }


        /**
         * User authentication
         *
         * @param request user's data
         * @return token
         */
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        var user = userService
                .getByEmail(request.getEmail());

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            ));
        } catch (BadCredentialsException e) {
            throw new BadRequestException("Incorrect password");
        }

        if (user.isEnabled()) {
            var jwt = jwtService.generateToken(user);

            logsService.info("User logged in",
                    String.format("User with id %d logged in", user.getId()),
                    mapper.toUserProfileDto(user),
                    null);

            return new JwtAuthenticationResponse(jwt);
        } else {
            throw new BadRequestException("User is not enabled");
        }
    }
}