package cat.cultura.backend.factories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.Locale;

public class GlobalMessageSource {

    @Autowired
    private static MessageSource messageSource;

    private static Locale locale;
    private static GlobalMessageSource instance;

    private GlobalMessageSource() {
        instance = this;
    }

    public static GlobalMessageSource getInstance() {
        return instance;
    }

    public static MessageSource getMessageSource() {
        return messageSource;
    }

    public static void setLocale(Locale locale) {
        GlobalMessageSource.locale = locale;

    }
    public void initialize() {
    }
}
