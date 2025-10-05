package orderscontrol.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("application.properties")) {

            if (input == null) {
                throw new RuntimeException("Файл application.properties не найден в classpath!");
            }

            properties.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки файла application.properties", e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}
