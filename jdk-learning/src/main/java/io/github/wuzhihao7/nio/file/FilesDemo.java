package io.github.wuzhihao7.nio.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FilesDemo {
    public static void main(String[] args) throws IOException {
        //检查文件是否存在
        Path path = Path.of("readme.md");
        System.out.println(Files.exists(path));
        //获取文件最后修改日期
        System.out.println(Files.getLastModifiedTime(path));
        //获取文件所有者
        System.out.println(Files.getOwner(path));
        //创建临时目录
        System.out.println(Files.createTempDirectory("prefix"));
        System.out.println(Files.createTempFile("prefix", ".jsp"));
        System.out.println(Files.createTempFile(null, null));

    }
}
