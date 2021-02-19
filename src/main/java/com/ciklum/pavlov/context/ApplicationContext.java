package com.ciklum.pavlov.context;


import com.ciklum.pavlov.models.User;
import com.ciklum.pavlov.util.io.CustomReader;
import com.ciklum.pavlov.util.io.CustomWriter;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;
import java.util.Objects;
import java.util.Properties;
import java.util.ResourceBundle;

@Data
@Slf4j
public class ApplicationContext {
    public static final String RESOURCE_BUNDLE_NAME = "application";
    private static ApplicationContext context;
    private CustomReader reader;
    private CustomWriter writer;
    private ResourceBundle resourceBundle;
    private User currentUser;
    @NonNull
    @Getter
    private Properties sqlProperties;

    public ApplicationContext() {
        resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME);
    }

    @Synchronized
    public static ApplicationContext getInstance() {
        return Objects.isNull(context) ? context = new ApplicationContext() : context;
    }


    public CustomReader getReader() {
        return reader;
    }

    public void setReader(@NonNull CustomReader reader) {
        this.reader = reader;
    }


    public CustomWriter getWriter() {
        return writer;
    }

    public void setWriter(@NonNull CustomWriter writer) {
        this.writer = writer;
    }


    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setLocale(@NonNull Locale locale) {
        resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME, locale);
    }
}