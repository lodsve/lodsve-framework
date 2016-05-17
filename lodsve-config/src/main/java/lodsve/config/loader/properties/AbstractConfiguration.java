package lodsve.config.loader.properties;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.ObjectUtils;

abstract class AbstractConfiguration implements Configuration {

    /**
     * Constant for the disabled list delimiter. This character is passed to the
     * list parsing methods if delimiter parsing is disabled. So this character
     * should not occur in string property values.
     */
    protected static final char DISABLED_DELIMITER = '\0';

    /** The default value for listDelimiter */
    private static char defaultListDelimiter = ',';

    /** Delimiter used to convert single values to lists */
    private char listDelimiter = defaultListDelimiter;

    /**
     * When set to true the given configuration delimiter will not be used
     * while parsing for this configuration.
     */
    private boolean delimiterParsingDisabled;

    /**
     * Whether the configuration should throw NoSuchElementExceptions or simply
     * return null when a property does not exist. Defaults to return null.
     */
    private boolean throwExceptionOnMissing;

	/**
	 * For configurations extending AbstractConfiguration, allow them to change
	 * the listDelimiter from the default comma (","). This value will be used
	 * only when creating new configurations. Those already created will not be
	 * affected by this change
	 * 
	 * @param delimiter
	 *            The new listDelimiter
	 */
	public static void setDefaultListDelimiter(char delimiter) {
		AbstractConfiguration.defaultListDelimiter = delimiter;
	}

	/**
	 * Retrieve the current delimiter. By default this is a comma (",").
	 * 
	 * @return The delimiter in use
	 */
	public static char getDefaultListDelimiter() {
		return AbstractConfiguration.defaultListDelimiter;
	}

	/**
	 * Change the list delimiter for this configuration.
	 * 
	 * Note: this change will only be effective for new parsings. If you want it
	 * to take effect for all loaded properties use the no arg constructor and
	 * call this method before setting the source.
	 * 
	 * @param listDelimiter
	 *            The new listDelimiter
	 */
	public void setListDelimiter(char listDelimiter) {
		this.listDelimiter = listDelimiter;
	}

	/**
	 * Retrieve the delimiter for this configuration. The default is the value
	 * of defaultListDelimiter.
	 * 
	 * @return The listDelimiter in use
	 */
	public char getListDelimiter() {
		return listDelimiter;
	}

	/**
	 * Determine if this configuration is using delimiters when parsing property
	 * values to convert them to lists of values. Defaults to false
	 * 
	 * @return true if delimiters are not being used
	 */
	public boolean isDelimiterParsingDisabled() {
		return delimiterParsingDisabled;
	}

	/**
	 * Set whether this configuration should use delimiters when parsing
	 * property values to convert them to lists of values. By default delimiter
	 * parsing is enabled
	 * 
	 * Note: this change will only be effective for new parsings. If you want it
	 * to take effect for all loaded properties use the no arg constructor and
	 * call this method before setting source.
	 * 
	 * @param delimiterParsingDisabled
	 *            a flag whether delimiter parsing should be disabled
	 */
	public void setDelimiterParsingDisabled(boolean delimiterParsingDisabled) {
		this.delimiterParsingDisabled = delimiterParsingDisabled;
	}

	/**
	 * Allows to set the {@code throwExceptionOnMissing} flag. This flag
	 * controls the behavior of property getter methods that return objects if
	 * the requested property is missing. If the flag is set to <b>false</b>
	 * (which is the default value), these methods will return <b>null</b>. If
	 * set to <b>true</b>, they will throw a {@code NoSuchElementException}
	 * exception. Note that getter methods for primitive data types are not
	 * affected by this flag.
	 * 
	 * @param throwExceptionOnMissing
	 *            The new value for the property
	 */
	public void setThrowExceptionOnMissing(boolean throwExceptionOnMissing) {
		this.throwExceptionOnMissing = throwExceptionOnMissing;
	}

	/**
	 * Returns true if missing values throw Exceptions.
	 * 
	 * @return true if missing values throw Exceptions
	 */
	public boolean isThrowExceptionOnMissing() {
		return throwExceptionOnMissing;
	}

	@Override
	public Configuration subset(String prefix) {
		return new SubsetConfiguration(this, prefix, ".");
	}
    
	@Override
	public boolean getBoolean(String key) {
		Boolean b = getBoolean(key, null);
		if (b != null) {
			return b.booleanValue();
		} else {
			throw new NoSuchElementException('\'' + key
					+ "' doesn't map to an existing object");
		}
	}

	@Override
	public boolean getBoolean(String key, boolean defaultValue) {
		return getBoolean(key, BooleanUtils.toBooleanObject(defaultValue))
				.booleanValue();
	}

	@Override
	public Boolean getBoolean(String key, Boolean defaultValue) {
		Object value = resolveContainerStore(key);

		if (value == null) {
			return defaultValue;
		} else {
			try {
				return PropertyConverter.toBoolean(value);
			} catch (ConversionException e) {
				throw new ConversionException('\'' + key
						+ "' doesn't map to a Boolean object", e);
			}
		}
	}

	@Override
	public byte getByte(String key) {
		Byte b = getByte(key, null);
		if (b != null) {
			return b.byteValue();
		} else {
			throw new NoSuchElementException('\'' + key
					+ " doesn't map to an existing object");
		}
	}

	@Override
	public byte getByte(String key, byte defaultValue) {
		return getByte(key, new Byte(defaultValue)).byteValue();
	}

	@Override
	public Byte getByte(String key, Byte defaultValue) {
		Object value = resolveContainerStore(key);

		if (value == null) {
			return defaultValue;
		} else {
			try {
				return PropertyConverter.toByte(value);
			} catch (ConversionException e) {
				throw new ConversionException('\'' + key
						+ "' doesn't map to a Byte object", e);
			}
		}
	}

	@Override
	public double getDouble(String key) {
		Double d = getDouble(key, null);
		if (d != null) {
			return d.doubleValue();
		} else {
			throw new NoSuchElementException('\'' + key
					+ "' doesn't map to an existing object");
		}
	}

	@Override
	public double getDouble(String key, double defaultValue) {
		return getDouble(key, new Double(defaultValue)).doubleValue();
	}

	@Override
	public Double getDouble(String key, Double defaultValue) {
		Object value = resolveContainerStore(key);

		if (value == null) {
			return defaultValue;
		} else {
			try {
				return PropertyConverter.toDouble(value);
			} catch (ConversionException e) {
				throw new ConversionException('\'' + key
						+ "' doesn't map to a Double object", e);
			}
		}
	}

	@Override
	public float getFloat(String key) {
		Float f = getFloat(key, null);
		if (f != null) {
			return f.floatValue();
		} else {
			throw new NoSuchElementException('\'' + key
					+ "' doesn't map to an existing object");
		}
	}

	@Override
	public float getFloat(String key, float defaultValue) {
		return getFloat(key, new Float(defaultValue)).floatValue();
	}

	@Override
	public Float getFloat(String key, Float defaultValue) {
		Object value = resolveContainerStore(key);

		if (value == null) {
			return defaultValue;
		} else {
			try {
				return PropertyConverter.toFloat(value);
			} catch (ConversionException e) {
				throw new ConversionException('\'' + key
						+ "' doesn't map to a Float object", e);
			}
		}
	}

	@Override
	public int getInt(String key) {
		Integer i = getInteger(key, null);
		if (i != null) {
			return i.intValue();
		} else {
			throw new NoSuchElementException('\'' + key
					+ "' doesn't map to an existing object");
		}
	}

	@Override
	public int getInt(String key, int defaultValue) {
		Integer i = getInteger(key, null);

		if (i == null) {
			return defaultValue;
		}

		return i.intValue();
	}

	@Override
	public Integer getInteger(String key, Integer defaultValue) {
		Object value = resolveContainerStore(key);

		if (value == null) {
			return defaultValue;
		} else {
			try {
				return PropertyConverter.toInteger(value);
			} catch (ConversionException e) {
				throw new ConversionException('\'' + key
						+ "' doesn't map to an Integer object", e);
			}
		}
	}

	@Override
	public long getLong(String key) {
		Long l = getLong(key, null);
		if (l != null) {
			return l.longValue();
		} else {
			throw new NoSuchElementException('\'' + key
					+ "' doesn't map to an existing object");
		}
	}

	@Override
	public long getLong(String key, long defaultValue) {
		return getLong(key, new Long(defaultValue)).longValue();
	}

	@Override
	public Long getLong(String key, Long defaultValue) {
		Object value = resolveContainerStore(key);

		if (value == null) {
			return defaultValue;
		} else {
			try {
				return PropertyConverter.toLong(value);
			} catch (ConversionException e) {
				throw new ConversionException('\'' + key
						+ "' doesn't map to a Long object", e);
			}
		}
	}

	@Override
	public short getShort(String key) {
		Short s = getShort(key, null);
		if (s != null) {
			return s.shortValue();
		} else {
			throw new NoSuchElementException('\'' + key
					+ "' doesn't map to an existing object");
		}
	}

	@Override
	public short getShort(String key, short defaultValue) {
		return getShort(key, new Short(defaultValue)).shortValue();
	}

	@Override
	public Short getShort(String key, Short defaultValue) {
		Object value = resolveContainerStore(key);

		if (value == null) {
			return defaultValue;
		} else {
			try {
				return PropertyConverter.toShort(value);
			} catch (ConversionException e) {
				throw new ConversionException('\'' + key
						+ "' doesn't map to a Short object", e);
			}
		}
	}

	@Override
	public BigDecimal getBigDecimal(String key) {
		BigDecimal number = getBigDecimal(key, null);
		if (number != null) {
			return number;
		} else if (isThrowExceptionOnMissing()) {
			throw new NoSuchElementException('\'' + key
					+ "' doesn't map to an existing object");
		} else {
			return null;
		}
	}

	@Override
	public BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
		Object value = resolveContainerStore(key);

		if (value == null) {
			return defaultValue;
		} else {
			try {
				return PropertyConverter.toBigDecimal(value);
			} catch (ConversionException e) {
				throw new ConversionException('\'' + key
						+ "' doesn't map to a BigDecimal object", e);
			}
		}
	}

	@Override
	public BigInteger getBigInteger(String key) {
		BigInteger number = getBigInteger(key, null);
		if (number != null) {
			return number;
		} else if (isThrowExceptionOnMissing()) {
			throw new NoSuchElementException('\'' + key
					+ "' doesn't map to an existing object");
		} else {
			return null;
		}
	}

	@Override
	public BigInteger getBigInteger(String key, BigInteger defaultValue) {
		Object value = resolveContainerStore(key);

		if (value == null) {
			return defaultValue;
		} else {
			try {
				return PropertyConverter.toBigInteger(value);
			} catch (ConversionException e) {
				throw new ConversionException('\'' + key
						+ "' doesn't map to a BigInteger object", e);
			}
		}
	}

	@Override
	public String getString(String key) {
		String s = getString(key, null);
		if (s != null) {
			return s;
		} else if (isThrowExceptionOnMissing()) {
			throw new NoSuchElementException('\'' + key
					+ "' doesn't map to an existing object");
		} else {
			return null;
		}
	}

	@Override
	public String getString(String key, String defaultValue) {
		Object value = resolveContainerStore(key);

		if (value instanceof String) {
			return (String) value;
		} else if (value == null) {
			return defaultValue;
		} else {
			throw new ConversionException('\'' + key
					+ "' doesn't map to a String object");
		}
	}

	public Set<String> getKeys(String prefix) {
		Set<String> keys = getKeys();
		Set<String> prefixKeys = new HashSet<String>();
		for (String key : keys) {
			if (key.startsWith(prefix + ".") || key.equals(prefix)) {
				prefixKeys.add(key);
			}
		}
		
		return prefixKeys;
	}

	@Override
	public Properties getProperties(String key) {
		return getProperties(key, null);
	}
	
	@Override
	public String[] getStringArray(String key) {
		Object value = getProperty(key);

		String[] array;

		if (value instanceof String) {
			array = new String[1];

			array[0] = (String) value;
		} else if (value instanceof List) {
			List<?> list = (List<?>) value;
			array = new String[list.size()];

			for (int i = 0; i < array.length; i++) {
				array[i] = ObjectUtils.toString(list.get(i), null);
			}
		} else if (value == null) {
			array = new String[0];
		} else if (isScalarValue(value)) {
			array = new String[1];
			array[0] = value.toString();
		} else {
			throw new ConversionException('\'' + key
					+ "' doesn't map to a String/List object");
		}
		return array;
	}

	@Override
	public List<Object> getList(String key) {
		return getList(key, new ArrayList<Object>());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getList(String key, List<?> defaultValue) {
		Object value = getProperty(key);
		List<Object> list;

		if (value instanceof String) {
			list = new ArrayList<Object>(1);
			list.add((String) value);
		} else if (value instanceof List) {
			list = new ArrayList<Object>();
			List<?> l = (List<?>) value;

			// add the interpolated elements in the new list
			for (Object elem : l) {
				list.add(elem);
			}
		} else if (value == null) {
			list = (List<Object>) defaultValue;
		} else if (value.getClass().isArray()) {
			return Arrays.asList((Object[]) value);
		} else if (isScalarValue(value)) {
			return Collections.singletonList((Object) value.toString());
		} else {
			throw new ConversionException('\'' + key
					+ "' doesn't map to a List object: " + value + ", a "
					+ value.getClass().getName());
		}
		return list;
	}

	private Properties getProperties(String key, Properties defaults) {
		/*
		 * Grab an array of the tokens for this key.
		 */
		String[] tokens = getStringArray(key);

		/*
		 * Each token is of the form 'key=value'.
		 */
		Properties props = defaults == null ? new Properties()
				: new Properties(defaults);
		for (String token : tokens) {
			int equalSign = token.indexOf('=');
			if (equalSign > 0) {
				String pkey = token.substring(0, equalSign).trim();
				String pvalue = token.substring(equalSign + 1).trim();
				props.put(pkey, pvalue);
			} else if (tokens.length == 1 && "".equals(token)) {
				// Semantically equivalent to an empty Properties
				// object.
				break;
			} else {
				throw new IllegalArgumentException('\'' + token
						+ "' does not contain an equals sign");
			}
		}
		return props;
	}
	
	private Object resolveContainerStore(String key) {
		Object value = getProperty(key);
		if (value != null) {
			if (value instanceof Collection) {
				Collection<?> collection = (Collection<?>) value;
				value = collection.isEmpty() ? null : collection.iterator().next();
			} else if (value.getClass().isArray() && Array.getLength(value) > 0) {
				value = Array.get(value, 0);
			}
		}

		return value;
	}

	/**
	 * Checks whether the specified object is a scalar value. This method is
	 * called by {@code getList()} and {@code getStringArray()} if the property
	 * requested is not a string, a list, or an array. If it returns
	 * <b>true</b>, the calling method transforms the value to a string and
	 * returns a list or an array with this single element. This implementation
	 * returns <b>true</b> if the value is of a wrapper type for a primitive
	 * type.
	 * 
	 * @param value
	 *            the value to be checked
	 * @return a flag whether the value is a scalar
	 * @since 1.7
	 */
	private boolean isScalarValue(Object value) {
		return ClassUtils.wrapperToPrimitive(value.getClass()) != null;
	}
}
