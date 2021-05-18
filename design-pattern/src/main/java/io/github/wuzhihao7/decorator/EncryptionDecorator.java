package io.github.wuzhihao7.decorator;

import java.nio.charset.StandardCharsets;

public class EncryptionDecorator extends DataSourceDecorator{
    public EncryptionDecorator(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void writeData(String data) {
        super.writeData(encode(data));
    }

    @Override
    public String readData() {
        return decode(super.readData());
    }

    private String encode(String data){
        return data + "_encode";
    }

    private String decode(String data){
        return data + "_decode";
    }
}
