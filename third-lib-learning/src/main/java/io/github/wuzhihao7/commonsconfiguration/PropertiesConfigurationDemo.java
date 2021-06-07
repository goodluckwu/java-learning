package io.github.wuzhihao7.commonsconfiguration;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.configuration2.ConfigurationLookup;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.BasicConfigurationBuilder;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.interpol.ConfigurationInterpolator;
import org.apache.commons.configuration2.interpol.Lookup;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PropertiesConfigurationDemo {
    public static void main(String[] args) throws ConfigurationException {
        Configurations configs = new Configurations();
        //Configure builder with lookups
        Parameters parameters = new Parameters();
        FileBasedConfigurationBuilder<PropertiesConfiguration> builder = configs.propertiesBuilder("config.properties");
        PropertiesConfiguration config = builder.getConfiguration();
        System.out.println(config.getString("greeting"));
        System.out.println(config.getString("greeting2"));
        System.out.println(config.getBigInteger("database.timeout"));
        //For object types like String, BigDecimal, or BigInteger this default behavior can be changed: If the setThrowExceptionOnMissing() method is called with an argument of true, these methods will behave like their primitive counter parts and also throw an exception if the passed in property key cannot be resolved.
        // Note: Unfortunately support for the throwExceptionOnMissing property is not always consistent: The methods getList() and getStringArray() do not evaluate this flag, but return an empty list or array if the requested property cannot be found. Maybe this behavior will be changed in a future major release.
        config.setThrowExceptionOnMissing(true);
//        System.out.println(config.getBigInteger("database.timeout2"));
        System.out.println(config.getInt("database.timeout"));
//        System.out.println(config.getInt("database.timeout2"));
        config.setListDelimiterHandler(new DefaultListDelimiterHandler('/'));
        config.addProperty("greeting2", "Hello, how are you?");
        config.addProperty("color.pie", new String[]{"#FF0000", "#00FF00", "#0000FF"});
        config.addProperty("color.graph", "#808080/#00FFCC/#6422FF");
        System.out.println(config.getString("greeting2"));
        System.out.println(config.getList("color.pie"));
        System.out.println(Arrays.toString(config.getStringArray("color.graph")));
        System.out.println(config.getString("color.pie"));
        System.out.println(config.getString("application.title"));
        System.out.println(config.getString("user.file"));
        System.out.println(config.getString("action.key"));
        System.out.println(config.getString("java.home"));
        System.out.println(config.getString("java.home2"));
        System.out.println(config.getString("java.home3"));
        System.out.println(config.getString("java.home4"));
        System.out.println(config.getString("var"));
        System.out.println(config.getProperty("var2"));
        //循环依赖报错
//        System.out.println(config.getString("var2"));

        //Create a map with defaults and one additional lookup
        Map<String, Lookup> lookupMap = new HashMap<>(ConfigurationInterpolator.getDefaultPrefixLookups());
        lookupMap.put("echo", varName -> "Value of variable" + varName);
        // Configure builder with lookups
        Parameters params = new Parameters();
        BasicConfigurationBuilder<PropertiesConfiguration> builder2 =
                new BasicConfigurationBuilder<>(
                        PropertiesConfiguration.class)
                        .configure(params.basic()
                                .setPrefixLookups(lookupMap));
        PropertiesConfiguration config2 = builder2.getConfiguration();
        config2.setProperty("config2", "${echo: config2}");
        System.out.println(config2.getString("config2"));
    }
}
