import ch.qos.logback.classic.db.DBAppender
import ch.qos.logback.core.*
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.db.DriverManagerConnectionSource

final String DB_DRIVER = System.getenv("LOGS_DATABASE_DRIVER")
final String DB_DRIVER_PATH = System.getenv("LOGS_DATABASE_DRIVER_PATH")
final String DB_URL = System.getenv("LOGS_DATABASE_URL")
final String DB_PORT = System.getenv("LOGS_DATABASE_PORT")
final String DB_NAME = System.getenv("LOGS_DATABASE_NAME")
final String DB_USERNAME = System.getenv("LOGS_DATABASE_USERNAME")
final String DB_PASSWORD = System.getenv("LOGS_DATABASE_PASSWORD")


// === APPENDERS ===
// Console appender
appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%date  %highlight(%-6level) [%15.15thread] %cyan(%-40.40logger{40}) : %msg%n"
    }
}

// Database appender
appender("DATABASE", DBAppender) {
    connectionSource(DriverManagerConnectionSource) {
        driverClass = DB_DRIVER_PATH
        url = String.format("jdbc:%s://%s:%s/%s", DB_DRIVER, DB_URL, DB_PORT, DB_NAME)
        user = DB_USERNAME
        password = DB_PASSWORD
    }
}

// === CONSOLE LOGGERS ===

// Logger for project level
logger("com.juniorstart", DEBUG, ["CONSOLE"])

//Logger for spring context
logger("org", INFO, ["CONSOLE"])

// === DATABASE LOGGERS ===

// Logger for project level
logger("com.juniorstart", DEBUG, ["DATABASE"])
