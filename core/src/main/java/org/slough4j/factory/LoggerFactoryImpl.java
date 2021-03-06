package org.slough4j.factory;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slough4j.dispatch.Dispatcher;
import org.slough4j.level.LevelStore;
import org.slough4j.logger.LoggerImpl;
import org.slough4j.logger.NopLogger;
import org.slough4j.model.Level;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides the implementation of ILoggerFactory bound to the SLF4J API.
 *
 * @author pmorie
 */
public class LoggerFactoryImpl implements ILoggerFactory {
    private final LevelStore levelStore;
    private final Dispatcher dispatcher;
    private final Map<String, Logger> loggerMap = new HashMap<String, Logger>();

    public LoggerFactoryImpl(LevelStore levelStore, Dispatcher dispatcher) {
        this.levelStore = levelStore;
        this.dispatcher = dispatcher;
    }

    @Override
    public Logger getLogger(String name) {
        Logger logger = loggerMap.get(name);

        if (logger == null) {
            logger = createLogger(name);
            loggerMap.put(name, logger);
        }

        return logger;
    }

    protected Logger createLogger(String name) {
        Level level = levelStore.findLevel(name);

        Logger logger;

        if (Level.OFF == level) {
            logger = new NopLogger(name);
        } else {
            logger = new LoggerImpl(name, level, dispatcher);
        }

        return logger;
    }
}
