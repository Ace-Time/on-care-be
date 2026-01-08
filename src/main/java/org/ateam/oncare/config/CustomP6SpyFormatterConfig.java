package org.ateam.oncare.config;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.springframework.stereotype.Component;

import java.util.Locale;

public class CustomP6SpyFormatterConfig implements MessageFormattingStrategy {
    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {

        sql = formatSql(category, sql);
        // 실행 시간(ms)와 쿼리를 로그백 형식에 맞춰 리턴
        return String.format("[%s] | %d ms | %s", category, elapsed, formatSql(category, sql));


    }

    private String formatSql(String category, String sql) {
        if (sql == null || sql.trim().equals("")) return sql;

        // 표기된 category가 statement 혹은 prepared일 경우만 포매팅
        if ("statement".equals(category) || "prepared".equals(category)) {
            String tmpsql = sql.trim().toLowerCase(Locale.ROOT);
            if (tmpsql.startsWith("create") || tmpsql.startsWith("alter") || tmpsql.startsWith("comment")) {
                sql = FormatStyle.DDL.getFormatter().format(sql);
            } else {
                sql = FormatStyle.BASIC.getFormatter().format(sql);
            }
        }
        return sql;
    }
}
