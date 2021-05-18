package io.github.wuzhihao7.decorator;

public class DecoratorDemo {
    public static void main(String[] args) {
        String data = "Decorator Pattern";

        CompressionDecorator compressionDecorator = new CompressionDecorator(new EncryptionDecorator(new FileDataSource()));
        compressionDecorator.writeData(data);

        System.out.println(compressionDecorator.readData());
    }
}
