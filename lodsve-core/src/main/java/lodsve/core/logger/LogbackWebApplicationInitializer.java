/*
 * Copyright (C) 2018  Sun.Hao
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package lodsve.core.logger;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.status.Status;
import lodsve.core.configuration.ApplicationProperties;
import lodsve.core.properties.relaxedbind.RelaxedBindFactory;
import lodsve.core.utils.StringUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.ILoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;
import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * logback初始化，要在类{@link lodsve.core.properties.ParamsHomeInitializer}之后.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class LogbackWebApplicationInitializer implements WebApplicationInitializer {
    private static final String CONFIG_FILE_SUFFIX = "xml";

    private static final Map<String, Level> LOG_LEVEL_LOGGERS;
    private String logConfig;

    static {
        LOG_LEVEL_LOGGERS = new HashMap<>();
        LOG_LEVEL_LOGGERS.put("lodsve", Level.DEBUG);
        LOG_LEVEL_LOGGERS.put("org.springframework", Level.TRACE);
        LOG_LEVEL_LOGGERS.put("org.apache.tomcat", Level.TRACE);
        LOG_LEVEL_LOGGERS.put("org.apache.catalina", Level.TRACE);
        LOG_LEVEL_LOGGERS.put("org.eclipse.jetty", Level.TRACE);
        LOG_LEVEL_LOGGERS.put("org.hibernate.tool.hbm2ddl", Level.TRACE);
        LOG_LEVEL_LOGGERS.put("org.hibernate.SQL", Level.DEBUG);
    }

    @Override
    public void onStartup(@NonNull ServletContext servletContext) throws ServletException {
        ApplicationProperties properties = new RelaxedBindFactory.Builder<>(ApplicationProperties.class).build();
        if (null != properties && MapUtils.isNotEmpty(properties.getLogLevel())) {
            properties.getLogLevel().forEach((k, v) -> LOG_LEVEL_LOGGERS.put(k, coerceLevel(v)));
        }

        if (null != properties) {
            logConfig = properties.getLogConfig();
        }

        initializeLogbackConfig();
        initializeFinalLoggingLevels();
    }

    private Level coerceLevel(String level) {
        if (Boolean.FALSE.toString().equalsIgnoreCase(level)) {
            return Level.OFF;
        }
        return Level.valueOf(level.toUpperCase(Locale.ENGLISH));
    }

    private LoggerContext getLoggerContext() {
        ILoggerFactory factory = StaticLoggerBinder.getSingleton().getLoggerFactory();
        Assert.isInstanceOf(LoggerContext.class, factory,
                String.format(
                        "LoggerFactory is not a Logback LoggerContext but Logback is on "
                                + "the classpath. Either remove Logback or the competing "
                                + "implementation (%s loaded from %s). If you are using "
                                + "WebLogic you will need to add 'org.slf4j' to "
                                + "prefer-application-packages in WEB-INF/weblogic.xml",
                        factory.getClass(), getLocation(factory)));
        return (LoggerContext) factory;
    }

    private Object getLocation(ILoggerFactory factory) {
        try {
            ProtectionDomain protectionDomain = factory.getClass().getProtectionDomain();
            CodeSource codeSource = protectionDomain.getCodeSource();
            if (codeSource != null) {
                return codeSource.getLocation();
            }
        } catch (SecurityException ex) {
            // Unable to determine location
        }
        return "unknown location";
    }

    private void stopAndReset(LoggerContext loggerContext) {
        loggerContext.stop();
        loggerContext.reset();
    }

    private void configureByResourceUrl(LoggerContext loggerContext, URL url) throws JoranException {
        if (url.toString().endsWith(CONFIG_FILE_SUFFIX)) {
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(loggerContext);
            configurator.doConfigure(url);
        } else {
            new ContextInitializer(loggerContext).configureByResource(url);
        }
    }

    private void initializeFinalLoggingLevels() {
        LOG_LEVEL_LOGGERS.forEach((name, level) -> {
            name = "ROOT".equalsIgnoreCase(name) ? null : name;
            setLogLevel(name, level);
        });
    }

    private void setLogLevel(String loggerName, Level level) {
        Logger logger = getLogger(loggerName);
        if (logger != null) {
            logger.setLevel(level);
        }
    }

    private Logger getLogger(String name) {
        LoggerContext factory = getLoggerContext();
        if (StringUtils.isBlank(name) || Logger.ROOT_LOGGER_NAME.equals(name)) {
            name = Logger.ROOT_LOGGER_NAME;
        }
        return factory.getLogger(name);

    }

    private void initializeLogbackConfig() {
        if (StringUtils.isBlank(logConfig)) {
            return;
        }

        LoggerContext context = getLoggerContext();
        stopAndReset(context);

        try {
            configureByResourceUrl(context, ResourceUtils.getURL(logConfig));
        } catch (Exception ex) {
            throw new IllegalStateException("Could not initialize Logback logging from " + logConfig, ex);
        }

        List<Status> statuses = context.getStatusManager().getCopyOfStatusList();
        StringBuilder errors = new StringBuilder();
        for (Status status : statuses) {
            if (status.getLevel() == Status.ERROR) {
                errors.append((errors.length() > 0) ? String.format("%n") : "");
                errors.append(status.toString());
            }
        }
        if (errors.length() > 0) {
            throw new IllegalStateException(String.format("Logback configuration error detected: %n%s", errors));
        }
    }
}
