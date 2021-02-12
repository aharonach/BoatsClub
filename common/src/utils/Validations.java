package utils;

import exceptions.InvalidInputException;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validations {
    public static final String dateFormat = "dd/MM/yyyy";
    public static final String dateTimeFormat = "dd/MM/yyyy H:mm";
    public static final String timeFormat = "H:mm";

    public static void checkEmptyString(String str) throws InvalidInputException {
        if (str != null && str.equals("")) {
            throw new InvalidInputException("Empty string.");
        }
    }

    public static void isPhoneNumber(String phoneNumber) throws InvalidInputException {
        if (!(phoneNumber.matches("^05[0-9]{8}$"))) {
            // phone number only digits, starts with "05" and total length of 10 digits.
            throw new InvalidInputException("Phone number is invalid.");
        }
    }

    public static void isFullName(String fullName) throws InvalidInputException {
        if (!fullName.matches("[a-z, A-Z]+")) {
            throw new InvalidInputException("name should contain only a-z, A-Z letters.");
        }
    }

    public static void isEmail(String email) throws InvalidInputException {
        Pattern pattern = Pattern.compile("^((?!\\.)[\\w-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$");
        Matcher mat = pattern.matcher(email);
        if (!mat.matches()) {
            throw new InvalidInputException("Invalid email.");
        }
    }

    public static void isValidAge(Integer age) throws InvalidInputException {
        if (!isNumberBetweenRange(age, 14, 99)) {
            throw new InvalidInputException("Age should be between 14 and 99.");
        }
    }

    public static boolean isNumberBetweenRange(int num, int left, int right) {
        return left <= num && num <= right;
    }

    public static void isBoolean(String userString) throws InvalidInputException {
        if (!(userString.equalsIgnoreCase("yes") || userString.equalsIgnoreCase("no"))) {
            throw new InvalidInputException("Boolean value is invalid.");
        }
    }

    public static void checkTimes(LocalTime startTime, LocalTime endTime) throws InvalidInputException {
        if (startTime.isAfter(endTime)) {
            throw new InvalidInputException("Start time is after end time.");
        }
    }

    public static void isXMLFilePath(String filepath) throws InvalidInputException {
        try {
            Paths.get(filepath);
            String regex = "([^\\s]+(\\.(?i)(xml))$)";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(filepath);
            if (!m.matches()) {
                throw new InvalidInputException("Path is not a valid XML file.");
            }
        } catch (InvalidPathException | NullPointerException ex) {
            throw new InvalidInputException("File path is invalid.");
        }
    }
}
