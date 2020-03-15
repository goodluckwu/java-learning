package com.technologysia;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class UnixTime {
    private final long value;

    public UnixTime(long value) {
        this.value = value;
    }

    public UnixTime(){
        this(System.currentTimeMillis() / 1000L + 2208988800L);
    }

    public long getValue(){
        return this.value;
    }

    @Override
    public String toString() {
        LocalDateTime localDateTime = Instant.ofEpochMilli((this.value - 2208988800L) * 1000L).atZone(ZoneId.systemDefault()).toLocalDateTime();
        return localDateTime.toString();
    }
}
