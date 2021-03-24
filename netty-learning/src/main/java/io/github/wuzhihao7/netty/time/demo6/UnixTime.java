package io.github.wuzhihao7.netty.time.demo6;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class UnixTime {
    private final long value;

    public UnixTime(){
        this(System.currentTimeMillis() / 1000L + 2208988800L);
    }

    public UnixTime(long value) {
        this.value = value;
    }

    public long value() {
        return value;
    }

    @Override
    public String toString() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli((value() - 2208988800L) * 1000L), ZoneId.systemDefault()).toString();
    }
}
