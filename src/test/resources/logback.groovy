import ch.qos.logback.core.*
import ch.qos.logback.classic.encoder.PatternLayoutEncoder

// === APPENDERS ===
// Console appender
appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%date  %highlight(%-6level) [%15.15thread] %cyan(%-40.40logger{40}) : %msg%n"
    }
}

// === CONSOLE LOGGERS ===

// Logger for project level
logger("com.juniorstart", DEBUG, ["CONSOLE"])

//Logger for spring context
logger("org", INFO, ["CONSOLE"])
