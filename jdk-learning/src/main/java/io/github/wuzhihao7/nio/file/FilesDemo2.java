package io.github.wuzhihao7.nio.file;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;

public class FilesDemo2 {
    public static void main(String[] args) throws IOException {
        Path root = Path.of("C:\\Users\\house\\Desktop\\a");
        Files.walkFileTree(root,new SimpleFileVisitor<>(){
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                System.out.println(Files.isDirectory(dir));
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
