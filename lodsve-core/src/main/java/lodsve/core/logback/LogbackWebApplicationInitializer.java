/*
 * Copyright © 2009 Sun.Hao(https://www.crazy-coder.cn/)
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lodsve.core.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.util.FileSize;
import ch.qos.logback.core.util.OptionHelper;
import lodsve.core.autoproperties.relaxedbind.RelaxedBindFactory;
import lodsve.core.configuration.ApplicationProperties;
import lodsve.core.configuration.LogbackConfig;
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
    private LogbackConfig config;

    static {
        LOG_LEVEL_LOGGERS = new HashMap<>();
        LOG_LEVEL_LOGGERS.put("lodsve", Level.TRACE);
        LOG_LEVEL_LOGGERS.put("org.springframework", Level.TRACE);
        LOG_LEVEL_LOGGERS.put("org.apache.tomcat", Level.TRACE);
        LOG_LEVEL_LOGGERS.put("org.apache.catalina", Level.TRACE);
        LOG_LEVEL_LOGGERS.put("org.eclipse.jetty", Level.TRACE);
        LOG_LEVEL_LOGGERS.put("org.hibernate.tool.hbm2ddl", Level.TRACE);
        LOG_LEVEL_LOGGERS.put("org.hibernate.SQL", Level.DEBUG);

        LOG_LEVEL_LOGGERS.put("org.apache.catalina.startup.DigesterFactory", Level.ERROR);
        LOG_LEVEL_LOGGERS.put("org.apache.catalina.util.LifecycleBase", Level.ERROR);
        LOG_LEVEL_LOGGERS.put("org.apache.coyote.http11.Http11NioProtocol", Level.WARN);
        LOG_LEVEL_LOGGERS.put("org.apache.sshd.common.util.SecurityUtils", Level.WARN);
        LOG_LEVEL_LOGGERS.put("org.apache.tomcat.util.net.NioSelectorPool", Level.WARN);
        LOG_LEVEL_LOGGERS.put("org.eclipse.jetty.util.component.AbstractLifeCycle", Level.ERROR);
        LOG_LEVEL_LOGGERS.put("org.hibernate.validator.internal.util.Version", Level.WARN);
    }

    @Override
    public void onStartup(@NonNull ServletContext servletContext) throws ServletException {
        ApplicationProperties properties = new RelaxedBindFactory.Builder<>(ApplicationProperties.class).build();

        if (null != properties && null != properties.getLogback() && MapUtils.isNotEmpty(properties.getLogback().getLevel())) {
            properties.getLogback().getLevel().forEach((k, v) -> LOG_LEVEL_LOGGERS.put(k, coerceLevel(v)));
        }

        if (null != properties) {
            config = properties.getLogback();
        }

        if (null != config && null != config.getConfig()) {
            initializeLogbackConfig();
            return;
        }

        initLoggerContext(getLoggerContext());
        initializeFinalLoggingLevels();
    }

    private Level coerceLevel(String level) {
        if (Boolean.FALSE.toString().equalsIgnoreCase(level)) {
            return Level.OFF;
        }
        return Level.valueOf(level.toUpperCase(Locale.ENGLISH));
    }

    private void initializeLogbackConfig() {
        if (StringUtils.isBlank(config.getConfig())) {
            return;
        }

        LoggerContext context = getLoggerContext();
        stopAndReset(context);

        try {
            configureByResourceUrl(context, ResourceUtils.getURL(config.getConfig()));
        } catch (Exception ex) {
            throw new IllegalStateException("Could not initialize Logback logging from " + config.getConfig(), ex);
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

    private void initLoggerContext(LoggerContext context) {
        stopAndReset(context);
        context.putProperty("LOG_LEVEL_PATTERN", "%5p");
        context.putProperty("LOG_DATEFORMAT_PATTERN", "yyyy-MM-dd HH:mm:ss.SSS");

        LogbackConfigurator configurator = new LogbackConfigurator(context);
        baseConfig(configurator);

        Appender<ILoggingEvent> consoleAppender = consoleAppender(configurator);
        if (config.getLogFile() != null) {
            Appender<ILoggingEvent> fileAppender = fileAppender(configurator, config.getLogFile());
            configurator.root(Level.INFO, consoleAppender, fileAppender);
        } else {
            configurator.root(Level.INFO, consoleAppender);
        }

        context.setPackagingDataEnabled(true);
    }

    private void baseConfig(LogbackConfigurator config) {
        config.conversionRule("clr", ColorConverter.class);
        config.conversionRule("wex", WhitespaceThrowableProxyConverter.class);
        config.conversionRule("wEx", ExtendedWhitespaceThrowableProxyConverter.class);
        config.logger("org.apache.catalina.startup.DigesterFactory", Level.ERROR);
        config.logger("org.apache.catalina.util.LifecycleBase", Level.ERROR);
        config.logger("org.apache.coyote.http11.Http11NioProtocol", Level.WARN);
        config.logger("org.apache.sshd.common.util.SecurityUtils", Level.WARN);
        config.logger("org.apache.tomcat.util.net.NioSelectorPool", Level.WARN);
        config.logger("org.eclipse.jetty.util.component.AbstractLifeCycle", Level.ERROR);
        config.logger("org.hibernate.validator.internal.util.Version", Level.WARN);
    }

    private <T> T getIfAbsent(T value, T defaultValue) {
        if (null == value) {
            return defaultValue;
        }

        return value;
    }

    private static final String CONSOLE_LOG_PATTERN = "%clr(%d{${LOG_DATEFORMAT_PATTERN:-yyyy-MM-dd HH:mm:ss.SSS}}){faint} %clr(${LOG_LEVEL_PATTERN:-%5p}) %clr(${PID:- }){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}";

    private Appender<ILoggingEvent> consoleAppender(LogbackConfigurator configurator) {
        ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<>();
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        String logPattern = getIfAbsent(config.getConsoleLogPattern(), CONSOLE_LOG_PATTERN);
        encoder.setPattern(OptionHelper.substVars(logPattern, configurator.getContext()));
        configurator.start(encoder);
        appender.setEncoder(encoder);
        configurator.appender("CONSOLE", appender);
        return appender;
    }

    private static final String FILE_LOG_PATTERN = "%d{${LOG_DATEFORMAT_PATTERN:-yyyy-MM-dd HH:mm:ss.SSS}} ${LOG_LEVEL_PATTERN:-%5p} ${PID:- } --- [%t] %-40.40logger{39} : %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}";

    private Appender<ILoggingEvent> fileAppender(LogbackConfigurator configurator, String logFile) {
        RollingFileAppender<ILoggingEvent> appender = new RollingFileAppender<>();
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        String logPattern = getIfAbsent(config.getFileLogPattern(), FILE_LOG_PATTERN);
        encoder.setPattern(OptionHelper.substVars(logPattern, configurator.getContext()));
        appender.setEncoder(encoder);
        configurator.start(encoder);
        appender.setFile(logFile);
        setRollingPolicy(appender, configurator, logFile);
        configurator.appender("FILE", appender);
        return appender;
    }

    private static final String MAX_FILE_SIZE = "10MB";

    private void setRollingPolicy(RollingFileAppender<ILoggingEvent> appender, LogbackConfigurator configurator, String logFile) {
        SizeAndTimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new SizeAndTimeBasedRollingPolicy<>();
        rollingPolicy.setFileNamePattern(logFile + ".%d{yyyy-MM-dd}.%i.gz");
        rollingPolicy.setMaxFileSize(FileSize.valueOf(getIfAbsent(config.getFileLogMaxSize(), MAX_FILE_SIZE)));
        rollingPolicy.setMaxHistory(getIfAbsent(config.getFileLogMaxHistory(), CoreConstants.UNBOUND_HISTORY));
        appender.setRollingPolicy(rollingPolicy);
        rollingPolicy.setParent(appender);
        configurator.start(rollingPolicy);
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
}
