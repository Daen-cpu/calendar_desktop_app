package org.example.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DBConfig {
    private static final Properties props = new Properties();

    static {
        try (InputStream input = DBConfig.class.getClassLoader().getResourceAsStream("db/db.properties")) {
            if (input == null) {
                throw new RuntimeException("Файл конфигурации db.properties не найден в classpath");
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки конфигурации базы данных", e);
        }
    }

    public static String getUrl() {
        return props.getProperty("db.url");
    }

    public static String getUser() {
        return props.getProperty("db.user");
    }

    public static String getPassword() {
        return props.getProperty("db.password");
    }
}
