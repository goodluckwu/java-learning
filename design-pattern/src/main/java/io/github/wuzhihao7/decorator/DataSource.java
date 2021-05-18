package io.github.wuzhihao7.decorator;

public interface DataSource {
    void writeData(String data);

    String readData();
}
