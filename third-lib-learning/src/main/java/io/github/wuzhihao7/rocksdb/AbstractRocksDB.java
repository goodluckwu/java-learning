package io.github.wuzhihao7.rocksdb;

import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

public class AbstractRocksDB {
    private final String path;
    private RocksDB rocksDB;

    public AbstractRocksDB(String path) {
        this.path = path;
        try {
            this.rocksDB = RocksDB.open(path);
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
    }
}
