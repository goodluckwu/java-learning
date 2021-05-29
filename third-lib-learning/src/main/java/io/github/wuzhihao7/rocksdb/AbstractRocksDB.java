package io.github.wuzhihao7.rocksdb;

import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class AbstractRocksDB {
    private static final Path PARENT = Path.of(System.getProperty("user.home"), "appdata", "rocksdb");
    private final String path;
    private RocksDB rocksDB;

    public AbstractRocksDB(String path) {
        this.path = PARENT.resolve(path).toString();
        System.out.println(this.path);
        try {
            Options options = new Options();
            options.setCreateIfMissing(true);
            this.rocksDB = RocksDB.open(options, this.path);
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
    }

    public RocksDB getRocksDB(){
        return rocksDB;
    }

    public static void main(String[] args) throws RocksDBException {
        System.out.println("你好");
        System.getProperties().list(System.out);
        AbstractRocksDB a = new AbstractRocksDB("test");
        a.getRocksDB().put("a".getBytes(StandardCharsets.UTF_8), "a".getBytes(StandardCharsets.UTF_8));
    }
}
