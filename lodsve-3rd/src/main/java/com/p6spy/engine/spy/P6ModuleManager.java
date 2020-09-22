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
package com.p6spy.engine.spy;

import com.p6spy.engine.common.P6LogQuery;
import com.p6spy.engine.common.P6Util;
import com.p6spy.engine.spy.option.*;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class P6ModuleManager {

    /**
     * 修改以引入外部的配置
     * add by SUNHAO 2017-12-25
     */
    private static final List<P6OptionsSource> OPTIONS_SOURCES = new ArrayList<>(16);
    private final Map<Class<? extends P6LoadableOptions>, P6LoadableOptions> allOptions = new HashMap<Class<? extends P6LoadableOptions>, P6LoadableOptions>();
    private final List<P6Factory> factories = new CopyOnWriteArrayList<>();
    private final P6MBeansRegistry mBeansRegistry = new P6MBeansRegistry();

    private final P6OptionsRepository optionsRepository = new P6OptionsRepository();

    private static P6ModuleManager instance;

    static {
        initMe();
    }

    /**
     * Used on the class load only (only once!)
     *
     * @throws MBeanRegistrationException   MBeanRegistrationException
     * @throws NotCompliantMBeanException   NotCompliantMBeanException
     * @throws MalformedObjectNameException MalformedObjectNameException
     * @throws InstanceNotFoundException    InstanceNotFoundException
     */
    private P6ModuleManager() throws MBeanRegistrationException, NotCompliantMBeanException, MalformedObjectNameException, InstanceNotFoundException {
        debug(this.getClass().getName() + " re/initiating modules started");

        // make sure the proper listener registration happens
        registerOptionChangedListener(new P6LogQuery());

        // hard coded - core module init - as it holds initial config
        final P6SpyLoadableOptions spyOptions = (P6SpyLoadableOptions) registerModule(new P6SpyFactory());
        if (null == spyOptions) {
            return;
        }

        loadDriversExplicitly(spyOptions);

        // configured modules init
        final Set<P6Factory> moduleFactories = spyOptions.getModuleFactories();
        if (null != moduleFactories) {
            for (P6Factory factory : spyOptions.getModuleFactories()) {
                registerModule(factory);
            }
        }

        optionsRepository.initCompleted();

        mBeansRegistry.registerMBeans(allOptions.values());

        for (P6OptionsSource optionsSource : OPTIONS_SOURCES) {
            optionsSource.postInit(this);
        }

        debug(this.getClass().getName() + " re/initiating modules done");
    }

    private synchronized static void initMe() {
        try {
            // 修改以引入外部的配置
            // add by SUNHAO 2017-12-25
            loadP6OptionsSources();

            cleanUp();

            instance = new P6ModuleManager();
            P6LogQuery.initialize();

        } catch (IOException | MBeanRegistrationException | InstanceNotFoundException | MalformedObjectNameException | NotCompliantMBeanException e) {
            handleInitEx(e);
        }
    }

    /**
     * 修改以引入外部的配置
     * add by SUNHAO 2017-12-25
     */
    private static void loadP6OptionsSources() throws IOException {
        OPTIONS_SOURCES.add(new SpyDotProperties());
        OPTIONS_SOURCES.add(new EnvironmentVariables());
        OPTIONS_SOURCES.add(new SystemProperties());

        ServiceLoader<P6OptionsSource> p6OptionsSources = ServiceLoader.load(P6OptionsSource.class);
        for (P6OptionsSource optionsSource : p6OptionsSources) {
            OPTIONS_SOURCES.add(optionsSource);
        }
    }

    private static void cleanUp() throws MBeanRegistrationException, MalformedObjectNameException {
        if (instance == null) {
            return;
        }

        for (P6OptionsSource optionsSource : P6ModuleManager.OPTIONS_SOURCES) {
            optionsSource.preDestroy(instance);
        }

        if (P6SpyOptions.getActiveInstance().getJmx()) {
            // unregister mbeans (to prevent naming conflicts)
            instance.mBeansRegistry.unregisterAllMBeans(P6SpyOptions.getActiveInstance().getJmxPrefix());
        }

        // clean table plz (we need to make sure that all the configured factories will be re-loaded)
        new DefaultJdbcEventListenerFactory().clearCache();
    }

    private synchronized P6LoadableOptions registerModule(P6Factory factory) {

        // re-register is not supported - skip silently
        for (P6Factory registeredFactory : factories) {
            if (registeredFactory.getClass().equals(factory.getClass())) {
                return null;
            }
        }

        final P6LoadableOptions options = factory.getOptions(optionsRepository);
        loadOptions(options);

        allOptions.put(options.getClass(), options);
        factories.add(factory);

        debug("Registered factory: " + factory.getClass().getName() + " with options: " + options.getClass().getName());

        return options;
    }

    /**
     * Returns loaded options. These are loaded in the right order:
     * <ul>
     * <li>default values</li>
     * <li>based on the order defined in the {@link #OPTIONS_SOURCES}</li>
     * </ul>
     *
     * @param options options
     */
    private void loadOptions(final P6LoadableOptions options) {
        // make sure to load defaults first
        options.load(options.getDefaults());

        // load the rest in the right order then
        for (P6OptionsSource optionsSource : OPTIONS_SOURCES) {
            Map<String, String> toLoad = optionsSource.getOptions();
            if (null != toLoad) {
                options.load(toLoad);
            }
        }

        // register to all the props then
        allOptions.put(options.getClass(), options);
    }

    public static P6ModuleManager getInstance() {
        return instance;
    }

    private static void handleInitEx(Exception e) {
        e.printStackTrace(System.err);
    }

    private void loadDriversExplicitly(P6SpyLoadableOptions spyOptions) {
        final Collection<String> driverNames = spyOptions.getDriverNames();
        if (null != driverNames) {
            for (String driverName : driverNames) {
                try {
                    // you really only need to load the driver if it is not a
                    // type 4 driver!
                    P6Util.forName(driverName).newInstance();
                } catch (Exception e) {
                    String err = "Error registering driver names: "
                            + driverNames + " \nCaused By: " + e.toString();
                    P6LogQuery.error(err);
                    throw new P6DriverNotFoundError(err);
                }
            }
        }
    }

    private void debug(String msg) {
        // not initialized yet => nowhere to log yet
        if (instance == null || factories.isEmpty()) {
            return;
        }

        P6LogQuery.debug(msg);
    }

    //
    // API methods
    //

    /**
     * @param optionsClass the class to get the options for.
     * @return the options instance depending on it's class.
     */
    @SuppressWarnings("unchecked")
    public <T extends P6LoadableOptions> T getOptions(Class<T> optionsClass) {
        return (T) allOptions.get(optionsClass);
    }

    /**
     * Reloads the {@link P6ModuleManager}. <br>
     * <br>
     * The idea is that whoever initiates this one causes it to start with the clean table. No
     * previously set values are kept (even those set manually - via jmx will be forgotten).
     */
    public void reload() {
        initMe();
    }

    public List<P6Factory> getFactories() {
        return factories;
    }

    public void registerOptionChangedListener(P6OptionChangedListener listener) {
        optionsRepository.registerOptionChangedListener(listener);
    }

    public void unregisterOptionChangedListener(P6OptionChangedListener listener) {
        optionsRepository.unregisterOptionChangedListener(listener);
    }

}
