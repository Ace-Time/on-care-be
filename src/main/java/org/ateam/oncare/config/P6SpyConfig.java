package org.ateam.oncare.config;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
public class P6SpyConfig {

    @Bean
    public MessageFormattingStrategy messageFormattingStrategy() {
        return (connectionId, now, elapsed, category, prepared, sql, url) -> {
            sql = formatSql(category, sql);
            // 실행 시간(ms)와 쿼리를 로그백 형식에 맞춰 리턴
            return String.format("[%s] | %d ms | %s", category, elapsed, formatSql(category, sql));
        };
    }

    private String formatSql(String category, String sql) {
        if (sql == null || sql.trim().equals("")) return sql;

        // Only format Statement and Prepared Statement
        if ("statement".equals(category) || "prepared".equals(category)) {
            String trimmedSql = sql.trim().toLowerCase(Locale.ROOT);
            if (trimmedSql.startsWith("create") || trimmedSql.startsWith("alter") || trimmedSql.startsWith("comment")) {
                sql = FormatStyle.DDL.getFormatter().format(sql);
            } else {
                sql = FormatStyle.BASIC.getFormatter().format(sql);
            }
        }
        return sql;
    }
}