package io.github.wuzhihao7.commonsconfiguration;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.nio.charset.StandardCharsets;

public class QuickStartGuide {
    public static void main(String[] args) {
        properties();
        xml();
        updateAndSaveProperty();
    }

    private static void updateAndSaveProperty() {
        Configurations configs = new Configurations();
        FileBasedConfigurationBuilder<PropertiesConfiguration> builder = configs.propertiesBuilder("config.properties");
        try {
            PropertiesConfiguration config = builder.getConfiguration();
            System.out.println(config.getString("greeting"));
            config.setProperty("newProperty", "newValue");
            builder.save();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    private static void xml() {
        Configurations configs = new Configurations();
        try {
            XMLConfiguration config = configs.xml("config.xml");
            System.out.println(config.getString("processing[@stage]"));
            System.out.println(config.getList(String.class, "processing.paths.path"));
            System.out.println(config.getString("processing.paths.path(1)"));
            System.out.println(config.getString("processing.paths2"));
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    private static void properties() {
        FileBasedConfigurationBuilder.setDefaultEncoding(PropertiesConfiguration.class, StandardCharsets.UTF_8.name());
        Configurations configurations = new Configurations();
        try {
            PropertiesConfiguration config = configurations.properties("config.properties");
            System.out.println(config.getString("database.host"));
            System.out.println(config.getString("database.host1"));
            System.out.println(config.getInt("database.port"));
            System.out.println(config.getBigInteger("database.port"));
            config.setThrowExceptionOnMissing(true);
            System.out.println(config.getBigInteger("database.port2"));
            System.out.println(config.getInt("database.port2"));
            System.out.println(config.getString("database.user"));
            System.out.println(config.getString("database.password"));
            System.out.println(config.getInt("database.timeout"));
            System.out.println(config.getString("greeting"));
            config.setConfigurationDecoder(s -> new String(s.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
            System.out.println(config.getEncodedString("greeting"));
            System.out.println(new String(config.getString("greeting").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }
}
