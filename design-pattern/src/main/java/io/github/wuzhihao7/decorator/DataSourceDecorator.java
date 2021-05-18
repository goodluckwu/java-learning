package io.github.wuzhihao7.decorator;

public class DataSourceDecorator implements DataSource{
    private final DataSource wrappee;

    public DataSourceDecorator(DataSource dataSource) {
        this.wrappee = dataSource;
    }

    @Override
    public void writeData(String data) {
        wrappee.writeData(data);
    }

    @Override
    public String readData() {
        return wrappee.readData();
    }
}
