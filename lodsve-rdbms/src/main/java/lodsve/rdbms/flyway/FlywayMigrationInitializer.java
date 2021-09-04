/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lodsve.rdbms.flyway;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;

import java.util.List;

/**
 * {@link InitializingBean} used to trigger {@link Flyway} migration via the
 * {@link FlywayMigrationStrategy}.
 *
 * @author Phillip Webb
 * @since 1.3.0
 */
public class FlywayMigrationInitializer implements InitializingBean, Ordered {

    private final List<Flyway> flyways;

    private final FlywayMigrationStrategy migrationStrategy;

    private int order = 0;

    /**
     * Create a new {@link FlywayMigrationInitializer} instance.
     *
     * @param flyways           the flyway instance
     * @param migrationStrategy the migration strategy or {@code null}
     */
    public FlywayMigrationInitializer(List<Flyway> flyways, FlywayMigrationStrategy migrationStrategy) {
        Assert.notEmpty(flyways, "Flyway must not be null");
        this.flyways = flyways;
        this.migrationStrategy = migrationStrategy;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        flyways.forEach(flyway -> {
            if (this.migrationStrategy != null) {
                this.migrationStrategy.migrate(flyway);
            } else {
                flyway.migrate();
            }
        });
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

}
