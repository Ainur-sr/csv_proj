package com.example.csv_proj.svc;

import org.springframework.util.StringUtils;

/**
 * @author Ainur Shigapov <ainur.shigapov@novardis.com>
 */

public class PhoneNumberUtil {

    public static String normalizePhoneNumber(final String originNum) {
        if (!StringUtils.hasText(originNum)) {
            return null;
        }

        final StringBuilder num = new StringBuilder(originNum);
        //* Если 12 цифр и запись начинается с "+", необходимо сохранить без "+".
        if (num.charAt(0) == '+') {
            num.delete(0, 1);
        }
        //* Если в телефоне 10 цифр - добавлять "7" в начале;
        if (num.length() == 10) {
            num.insert(0, '7');
        } else if (num.length() == 11 && num.charAt(0) == '8') {
            //* Если 11 цифр, но начинаются с "8" - конвертировать "8",
            // если с "7" - оставлять в переданном виде.
            num.replace(0, 1, "7");
        }
        return num.toString();
    }
}
