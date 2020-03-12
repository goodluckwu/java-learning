

import ch.qos.logback.classic.encoder.PatternLayoutEncoder

appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%gray(%d{yyyy-MM-dd HH:mm:ss.SSS}) -%highlight(%5p) %boldRed(%X{PID}) --- [%boldYellow(%15.15t)] %cyan(%-40.40logger{39}) : %m%n"
    }
}
root(INFO, ["CONSOLE"])