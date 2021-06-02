package io.github.wuzhihao7.configuration;

import java.time.LocalDate;
import java.util.Properties;

/**
 * 读取全局性配置信息的接口
 */
public interface Configuration {
    /**
     * 获取指定的键对应的字符串键值。
     * @param key 键
     * @param defaultValue 默认值
     * @return 参数key对应的键值。如果键值不存在，则返回参数defaultValue代表的值。
     */
    String getString(String key, String defaultValue);

    /**
     * 获取指定的键对应的字符串键值。
     * @param key 键
     * @return 参数key对应的键值。如果键值不存在则返回空字符串。
     */
    String getString(String key);

    /**
     * 获取指定的键对应的整数型键值
     * @param key 键
     * @param defaultValue 默认值
     * @return 参数key对应的键值。如果键值不存在，则返回参数defaultValue代表的默认值。
     */
    int getInt(String key, int defaultValue);

    /**
     * 获取指定的键对应的整数型键值。
     * @param key 键
     * @return 参数key对应的键值。如果键值不存在，则返回0。
     */
    int getInt(String key);

    /**
     * 获取指定的键对应的长整型键值。
     * @param key 键
     * @param defaultValue 默认值
     * @return 参数key对应的键值。如果键值不存在，则返回参数defaultValue代表的默认值。
     */
    long getLong(String key, long defaultValue);

    /**
     * 获取指定的键对应的长整型键值。
     * @param key 键
     * @return 参数key对应的键值。如果键值不存在，则返回0L。
     */
    long getLong(String key);

    /**
     * 获取指定键对应的双精度型键值。
     * @param key 键
     * @param defaultValue 默认值
     * @return 参数key对应的键值。如果键值不存在，则返回参数defaultValue代表的默认值。
     */
    double getDouble(String key, double defaultValue);

    /**
     * 获取指定的键对应的双精度型键值.
     * @param key 键
     * @return 参数key对应的键值。如果键值不存在，则返回od。
     */
    double getDouble(String key);

    /**
     * 获取指定键对应的布尔型键值。
     * @param key 键
     * @param defaultValue 默认值
     * @return 参数key对应的键值。如果键值不存在，则返回参数defaultValue代表的默认值。
     */
    boolean getBoolean(String key, boolean defaultValue);

    /**
     * 获取指定的键对应的布尔型键值。
     * @param key 键
     * @return 参数key对应的键值。如果键值不存在，则返回false。
     */
    boolean getBoolean(String key);

    /**
     * 获取指定的键对应的日期型键值.
     * @param key 键
     * @param defaultValue 默认值
     * @return 参数key对应的键值。如果键值不存在，则返回参数defaultValue代表的默认值。
     */
    LocalDate getDate(String key, LocalDate defaultValue);

    /**
     * 获取指定的键对应的的=日期型键值。
     * @param key 键
     * @return 参数key对应的键值。如果键值不存在，则返回null。
     */
    LocalDate getDate(String key);

    /**
     * 获取指定的键对应的对象型键值。
     * @param key 键
     * @param objectClass 对象的类字节码
     * @param defaultValue 默认值
     * @param <T> 配置项的值类型
     * @return 参数key对应的键值。如果键值不存在，则返回参数defaultValue代表的默认值。
     */
    <T> T getProperty(String key, Class<T> objectClass, T defaultValue);

    /**
     * 获取指定的键对应的对象型键值。
     * @param key 键
     * @param objectClass 对象的类字节码
     * @param <T> 配置项的值类型
     * @return 参数key对应的键值。如果键值不存在，则返回null。
     */
    <T> T getObject(String key, Class<T> objectClass);

    /**
     * 设置指定配置项的字符串型键值。
     * @param key 配置项的键
     * @param value 配置项的值
     */
    void setString(String key, String value);

    /**
     * 设置指定配置项的整型键值。
     * @param key 配置项的键
     * @param value 配置项的值
     */
    void setInt(String key, int value);

    /**
     * 设置指定配置项的长整型键值。
     * @param key 配置项的键
     * @param value 配置项的值
     */
    void setLong(String key, long value);

    /**
     * 设置指定配置项的双精度类型键值。
     * @param key 配置项的键
     * @param value 配置项的值
     */
    void setDouble(String key, double value);

    /**
     * 设置指定配置项的布尔型键值。
     * @param key 配置项的键
     * @param value 配置项的值
     */
    void setBoolean(String key, boolean value);

    /**
     * 设置指定配置项的日期型键值。
     * @param key 配置项的键
     * @param value 配置项的值
     */
    void setDate(String key, LocalDate value);

    /**
     * 设置指定配置项的对象型键值。
     * @param key 配置项的键
     * @param value 配置项的值
     */
    void setObject(String key, Object value);

    /**
     * 获取所有属性
     * @return properties形式的配置项
     */
    Properties getProperties();

    /**
     * 从持久化源中获取最新配置，更新当前配置
     */
    void load();
}
