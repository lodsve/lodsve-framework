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
package lodsve.core.autoproperties.env;

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
