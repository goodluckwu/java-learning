

import ch.qos.logback.classic.encoder.PatternLayoutEncoder

def pid =

appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%gray(%d{yyyy-MM-dd HH:mm:ss.SSS}) -%highlight(%5p) --- [%boldYellow(%15.15t)] %cyan(%-40.40logger{39}) : %m%n"
    }
}
root(INFO, ["CONSOLE"])