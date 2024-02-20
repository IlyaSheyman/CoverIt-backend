package main_service.config.passay;

import lombok.SneakyThrows;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.EnglishSequenceData;
import org.passay.IllegalSequenceRule;
import org.passay.LengthRule;
import org.passay.MessageResolver;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.PropertiesMessageResolver;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static main_service.constants.Constants.MAX_USER_PASSWORD_LENGTH;
import static main_service.constants.Constants.MIN_USER_PASSWORD_LENGTH;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @SneakyThrows
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        Properties props = new Properties();
        InputStream inputStream = getClass()
                .getClassLoader().getResourceAsStream("passay.properties");
        props.load(inputStream);
        MessageResolver resolver = new PropertiesMessageResolver(props);

        PasswordValidator validator = new PasswordValidator(resolver, Arrays.asList(
                new LengthRule(MIN_USER_PASSWORD_LENGTH, MAX_USER_PASSWORD_LENGTH),
                // at least one upper-case character
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                // at least one lower-case character
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                // at least one digit character
                new CharacterRule(EnglishCharacterData.Digit, 1),
                // at least one special character
                new CharacterRule(EnglishCharacterData.Special, 1),
                new WhitespaceRule(),
                // rejects passwords that contain a sequence of >= 5 characters alphabetical  (e.g. abcdef)
                new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 5, false),
                // rejects passwords that contain a sequence of >= 5 characters numerical   (e.g. 12345)
                new IllegalSequenceRule(EnglishSequenceData.Numerical, 5, false)
        ));

        RuleResult result = validator.validate(new PasswordData(password));

        if (result.isValid()) {
            return true;
        }

        List<String> messages = validator.getMessages(result);
        String messageTemplate = String.join(",", messages);
        context
                .buildConstraintViolationWithTemplate(messageTemplate)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();

        return false;
    }
}