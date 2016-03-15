package pl.edu.agh.ds.tictactoe.noteboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesLoader.class);

    public static Properties loadProperties(String filePath) throws IOException {
        Properties properties = new Properties();
        try (FileInputStream in = new FileInputStream(filePath)) {
            properties.load(in);
            LOGGER.info("Loaded properties from file: " + filePath);
        }
        return properties;
    }
}
