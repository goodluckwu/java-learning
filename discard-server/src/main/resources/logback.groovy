

import ch.qos.logback.classic.encoder.PatternLayoutEncoder

appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} %5p %X{PID} --- [%15.15t] %-40.40logger{39} : %m%n"
    }
}
root(INFO, ["CONSOLE"])