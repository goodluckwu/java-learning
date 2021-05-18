package io.github.wuzhihao7.decorator;

public class CompressionDecorator extends DataSourceDecorator{

    public CompressionDecorator(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void writeData(String data) {
        super.writeData(compress(data));
    }

    private String compress(String data) {
        return data + "_compress";
    }

    @Override
    public String readData() {
        return decompress(super.readData());
    }

    private String decompress(String data) {
        return data + "_decompress";
    }
}
