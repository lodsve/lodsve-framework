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
