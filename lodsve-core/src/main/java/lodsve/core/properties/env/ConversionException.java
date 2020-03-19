/*
 * Copyright (C) 2019 Sun.Hao(https://www.crazy-coder.cn/)
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

package lodsve.core.properties.env;

/**
 * Exception thrown when a property is incompatible with the type requested.
 *
 * @author Emmanuel Bourg
 * @version $Id: ConversionException.java 1208806 2011-11-30 21:34:11Z oheger $
 * @since 1.0
 */
class ConversionException extends RuntimeException {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = -5167943099293540392L;

    /**
     * Constructs a new {@code ConversionException} without specified detail
     * message.
     */
    public ConversionException() {
        super();
    }

    /**
     * Constructs a new {@code ConversionException} with specified detail
     * message.
     *
     * @param message the error message
     */
    public ConversionException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code ConversionException} with specified nested
     * {@code Throwable}.
     *
     * @param cause the exception or error that caused this exception to be thrown
     */
    public ConversionException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new {@code ConversionException} with specified detail
     * message and nested {@code Throwable}.
     *
     * @param message the error message
     * @param cause   the exception or error that caused this exception to be thrown
     */
    public ConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
