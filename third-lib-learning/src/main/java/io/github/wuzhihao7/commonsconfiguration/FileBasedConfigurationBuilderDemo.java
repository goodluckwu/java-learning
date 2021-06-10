package io.github.wuzhihao7.commonsconfiguration;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.FileBasedBuilderParameters;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class FileBasedConfigurationBuilderDemo {
    public static void main(String[] args) {
        Parameters parameters = new Parameters();
        File propertiesFile = new File("config.properties");

        FileBasedConfigurationBuilder<PropertiesConfiguration> builder = new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
                .configure(parameters.fileBased().setFile(propertiesFile));

        try {
            PropertiesConfiguration config = builder.getConfiguration();
            System.out.println(new String(config.getString("greeting").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
            config.addProperty("newProperty", "new");
            config.setProperty("updateProperty", "changedValue2");
            builder.save();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }
}
