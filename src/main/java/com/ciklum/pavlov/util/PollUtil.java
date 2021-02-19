package com.ciklum.pavlov.util;

import com.ciklum.pavlov.models.Product;
import com.ciklum.pavlov.util.io.CustomReader;
import com.ciklum.pavlov.util.io.CustomWriter;
import lombok.NonNull;

import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import static com.ciklum.pavlov.constants.NotificationConstants.INVALID_POSITIVE_INTEGER;
import static com.ciklum.pavlov.constants.NotificationConstants.INVALID_PRODUCT_STATUS;

public class PollUtil {
    public static final String ANY_WORD = "^[a-zA-Z]+$";
    public static final String LOGIN = "^[a-zA-Z]{6,}$";
    public static final String PASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    public static final String SINGLE_DIGIT = "^[0-9]$";
    public static final String WRONG_WORD_NOTE = "The entered value is not correct!";
    public static final String PRODUCT_STATUSES = String.format("Valid statuses: |%s|%s|%s|", "out_of_stock", "in_stock", "running_low");
    public static final String WRONG_PRODUCT_STATUS_NOTE = "Entered wrong product status! " + PRODUCT_STATUSES;
    public static final String INVALID_WORD = "poll.util.invalidWord";
    public static final String WRONG_POSITIVE_INTEGER = String.format("The number should be between 0 and %s", Integer.MAX_VALUE);

    private PollUtil() {
    }

    @NonNull
    public static String pollString(CustomWriter writer, CustomReader reader, String regExp, Optional<Set<String>> supportedValues, Optional<ResourceBundle> resource) {
        String input;
        while (!isValidAnswer(input = reader.readLine(), supportedValues, regExp)) {
            String notificationMsg = resource.map(bundle -> bundle.getString(INVALID_WORD)).orElse(WRONG_WORD_NOTE);
            writer.println(notificationMsg);
        }
        return input;
    }

    public static int pollInteger(@NonNull CustomReader reader) {
        String value;
        int intValue = -1;
        boolean isValidAnswer = false;
        while (!isValidAnswer) {
            isValidAnswer = Boolean.TRUE;
            value = reader.readLine();
            value = value.trim();
            try {
                intValue = Integer.parseInt(value);
            } catch (NumberFormatException ex) {
                isValidAnswer = false;
            }
        }
        return intValue;
    }

    public static int pollPositiveInteger(CustomWriter writer, CustomReader reader, @NonNull Optional<ResourceBundle> resource) {
        int positiveInt;
        while ((positiveInt = pollInteger(reader)) < 0) {
            String notificationMsg = resource.map(bundle -> bundle.getString(INVALID_POSITIVE_INTEGER)).orElse(WRONG_POSITIVE_INTEGER);
            writer.println(notificationMsg);
        }
        return positiveInt;
    }


    public static Product.ProductStatus pollProductStatus(CustomWriter writer, CustomReader reader, @NonNull Optional<ResourceBundle> resource) {
        String productStatus;
        while (!isCorrectProductStatus(productStatus = reader.readLine())) {
            resource.ifPresent((rs)->writer.println(rs.getString(INVALID_PRODUCT_STATUS)));
        }
        return extractProductStatus(productStatus);
    }

    private static Product.ProductStatus extractProductStatus(final String stringRepresentation) {
        return Arrays.stream(Product.ProductStatus.values())
                .filter(el -> el.name().equalsIgnoreCase(stringRepresentation))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    private static boolean isCorrectProductStatus(final String productStatus) {
        return Arrays.stream(Product.ProductStatus.values()).anyMatch(el -> productStatus.equalsIgnoreCase(el.name()));
    }

    private static boolean isValidAnswer(final String answer, @NonNull Optional<Set<String>> supportedValues, String regexp) {
        return supportedValues.map(strings -> (strings
                .stream()
                .anyMatch((element -> element.equals(answer)))
                && answer.matches(regexp))).orElseGet(() -> answer.matches(regexp));
    }
}
