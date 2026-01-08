package org.ateam.oncare.config.logutil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogMaskingUtils {

    // JSON 형식에서 password, pwd, cardNo 등의 값을 찾아 마스킹 (**로 변경)
    private static final String JSON_MASKING_REGEX =
            "(\"password\"|\"passwd\"|\"pwd\"|\"cardNo\"|\"accessToken\")\\s*:\\s*\"([^\"]+)\"";

    private static final Pattern PATTERN = Pattern.compile(JSON_MASKING_REGEX, Pattern.CASE_INSENSITIVE);

    public static String maskingSensitiveData(String jsonString) {
        if (jsonString == null || jsonString.isEmpty()) {
            return jsonString;
        }

        Matcher matcher = PATTERN.matcher(jsonString);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            // 그룹1(키)은 유지하고, 그룹2(값)를 ****로 치환
            matcher.appendReplacement(sb, matcher.group(1) + ":\"****\"");
        }
        matcher.appendTail(sb);

        return sb.toString();
    }
}