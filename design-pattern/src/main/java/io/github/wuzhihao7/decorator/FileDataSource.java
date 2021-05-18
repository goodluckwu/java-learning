package io.github.wuzhihao7.decorator;

public class FileDataSource implements DataSource{
    private String data;
    @Override
    public void writeData(String data) {
        this.data = data;
        System.out.printf("写入数据: %s%n", data);
    }

    @Override
    public String readData() {
        return data;
    }
}
