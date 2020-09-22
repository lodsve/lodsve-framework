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

/**
 * Strategy used to initialize {@link Flyway} migration. Custom implementations may be
 * registered as a {@code @Bean} to override the default migration behavior.
 *
 * @author Andreas Ahlenstorf
 * @author Phillip Webb
 */
@FunctionalInterface
public interface FlywayMigrationStrategy {

    /**
     * Trigger flyway migration.
     *
     * @param flyway the flyway instance
     */
    void migrate(Flyway flyway);

}
