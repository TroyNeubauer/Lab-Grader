package com.troy.labgrader;

import java.io.*;
import java.lang.reflect.*;
import java.net.URISyntaxException;
import java.nio.*;
import java.util.*;

import org.joda.time.*;
import org.joda.time.format.PeriodFormat;

import sun.misc.Unsafe;

/**
 * A class that has some static miscellaneous utilities.
 * 
 * @author Troy Neubauer
 *
 */
@SuppressWarnings("restriction")
public class MiscUtil {

	private static final Unsafe unsafe = retriveUnsafe();

	private static final String UNSAFE_CLASS = "sun.misc.Unsafe", DISABLE_UNSAFE_ARG = "TBDisableUnsafe";

	private static final long NIO_BUFFER_ADDRESS_OFFSET = findBufferAddress();

	private static final Field STRING_VALUE;
	private static final Constructor<String> STRING_CHAR_CONSTRUCTOR;

	static {
		Field f = null;
		for (Field field : String.class.getDeclaredFields()) {
			if (field.getType() == char[].class) {
				f = field;
				break;
			}
		}
		if (f == null)
			throw new Error("Unable to locate char[] inside the string class");
		f.setAccessible(true);
		STRING_VALUE = f;

		Constructor<String> c = null;
		for (Constructor<?> constructor : String.class.getDeclaredConstructors()) {
			if (constructor.getGenericParameterTypes().length == 2 && constructor.getParameterTypes()[0] == char[].class) {
				c = (Constructor<String>) constructor;
				break;
			}
		}
		if (c == null)
			throw new Error("Unable to locate the String(char[]) constructor");
		c.setAccessible(true);
		STRING_CHAR_CONSTRUCTOR = c;
	}

	public static boolean isBigEndian() {
		return ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;
	}

	public static boolean isLittleEndian() {
		return ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN;
	}

	public static Class<?> getGenericType(Object obj) {
		return (Class<?>) ((ParameterizedType) obj.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public static boolean isClassLoaded(String binaryName) {
		try {
			Method m = ClassLoader.class.getDeclaredMethod("findLoadedClass", new Class[] { String.class });
			m.setAccessible(true);

			return m.invoke(MiscUtil.class.getClassLoader(), binaryName) != null;
		} catch (Exception e) {
			// Ignore
		}
		return false;
	}

	public static boolean isClassDefualtJavaClass(Class<?> clazz) {
		return clazz.getClassLoader() == null;
	}

	/**
	 * Returns an Enum object representing the enum declared in class {@code class} with the ordinal {@code ordinal}
	 * 
	 * @param clazz The class to look in. Must be an enum class
	 * @param ordinal The ordinal of the enum to look for
	 * @return the enum declared in class {@code class} with the ordinal {@code ordinal} {@link Enum#ordinal()}<br>
	 *         {@link Enum}
	 */
	public static <T> T getEnum(Class<T> clazz, int ordinal) {
		assert clazz.isEnum();
		try {
			Method m = clazz.getDeclaredMethod("values");
			Object values = m.invoke(null);
			Enum<?>[] thing = (Enum<?>[]) values;
			for (Enum<?> e : thing) {
				if (e.ordinal() == ordinal)
					return (T) e;
			}
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(clazz + " is not an enum!");
		} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			System.err.println("Illegal enum reflection! " + clazz + " requested ordinal " + ordinal);
		}
		return null;

	}

	/**
	 * Returns {@code true} if the class a contains b in the hierarchy
	 * 
	 * @param baseClass The class's hierarchy to use
	 * @param lookFor The class to compare to
	 * @return {@code true} if the class a contains b in a superclass, otherwise false
	 */
	public static boolean classSharesSuperClassOrInterface(Class<?> baseClass, Class<?> lookFor) {
		if (baseClass == lookFor)
			return true;
		Class<?> origionalBaseClass = baseClass;
		while ((baseClass = baseClass.getSuperclass()) != null)
			if (baseClass == lookFor)
				return true;
		return classImplementsInterface(origionalBaseClass, lookFor);
	}

	public static boolean classImplementsInterface(Class<?> baseClass, Class<?> lookFor) {
		for (Class<?> interfac : baseClass.getInterfaces()) {
			if (interfac == lookFor)
				return true;
			if (classImplementsInterface(interfac, lookFor))
				return true;
		}
		return false;
	}

	public static Process runClass(Class<?> type) throws IOException {
		return runClass(type, new ProcessBuilder(), new String[0]);
	}

	public static Process runClass(Class<?> type, ProcessBuilder builder, String[] args) throws IOException {
		File location;
		try {
			location = new File(type.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		/*
		 * if (location.isFile()) { assert FilenameUtils.isExtension(location.getName(), "jar");
		 * 
		 * builder.command("java", "-cp", location.getAbsolutePath(), type.getName());
		 * 
		 * } else {// Is directory
		 */
		builder.command("java", "-cp", "\"" + location.getAbsolutePath() + "\"", type.getName());
		// }
		for (String arg : args)// Add extra args
			builder.command().add(arg);

		System.out.println("Running: " + builder.command());
		return builder.start();
	}

	private static final Object helperArray[] = new Object[1];

	/**
	 * Returns the memory address of the given object
	 * 
	 * @param obj The object to locate the address of
	 * @return The memory address of Object obj
	 */
	public static long memoryAddress(Object obj) {
		if (unsafe == null)
			throw new IllegalStateException("Unsafe is NOT unsupported!! So the memory address cannot be retrived!!!");
		helperArray[0] = obj;
		long baseOffset = Unsafe.ARRAY_OBJECT_BASE_OFFSET;
		long addressOfObject = unsafe.getLong(helperArray, baseOffset);
		return addressOfObject;
	}

	/**
	 * Returns JVM type signature for given class.
	 */
	public static String getClassSignature(Class<?> cl) {
		StringBuilder sbuf = new StringBuilder();
		while (cl.isArray()) {
			sbuf.append('[');
			cl = cl.getComponentType();
		}
		if (cl.isPrimitive()) {
			if (cl == Integer.TYPE) {
				sbuf.append('I');
			} else if (cl == Byte.TYPE) {
				sbuf.append('B');
			} else if (cl == Long.TYPE) {
				sbuf.append('J');
			} else if (cl == Float.TYPE) {
				sbuf.append('F');
			} else if (cl == Double.TYPE) {
				sbuf.append('D');
			} else if (cl == Short.TYPE) {
				sbuf.append('S');
			} else if (cl == Character.TYPE) {
				sbuf.append('C');
			} else if (cl == Boolean.TYPE) {
				sbuf.append('Z');
			} else if (cl == Void.TYPE) {
				sbuf.append('V');
			} else {
				throw new InternalError();
			}
		} else {
			sbuf.append('L' + cl.getName().replace('.', '/') + ';');
		}
		return sbuf.toString();
	}

	/**
	 * Gets the class for a signature (only handles one signature at a time)
	 * 
	 * @param signature The signature
	 * @return The class representing the specified signature
	 * @throws ClassNotFoundException
	 */
	public static Class<?> getClassFromSignature(String signature) throws ClassNotFoundException {
		if (signature.isEmpty())
			throw new IllegalArgumentException("Signature cannot be empty!");
		if (signature.charAt(0) == '[') {
			if (signature.contains("L"))
				return Array.newInstance(Class.forName(signature.substring(1, signature.length()).replace("/", ".")), 1).getClass();
			int arrayDimensions = signature.lastIndexOf('[') + 1;
			char type = signature.charAt(signature.length() - 1);
			if (arrayDimensions == 1) {
				if (type == 'B')
					return byte[].class;
				if (type == 'S')
					return short[].class;
				if (type == 'C')
					return char[].class;
				if (type == 'I')
					return int[].class;
				if (type == 'J')
					return long[].class;
				if (type == 'F')
					return float[].class;
				if (type == 'D')
					return double[].class;
				if (type == 'Z')
					return boolean[].class;
			} else if (arrayDimensions == 2) {
				if (type == 'B')
					return byte[][].class;
				if (type == 'S')
					return short[][].class;
				if (type == 'C')
					return char[][].class;
				if (type == 'I')
					return int[][].class;
				if (type == 'J')
					return long[][].class;
				if (type == 'F')
					return float[][].class;
				if (type == 'D')
					return double[][].class;
				if (type == 'Z')
					return boolean[][].class;
			} else if (arrayDimensions == 3) {
				if (type == 'B')
					return byte[][][].class;
				if (type == 'S')
					return short[][][].class;
				if (type == 'C')
					return char[][][].class;
				if (type == 'I')
					return int[][][].class;
				if (type == 'J')
					return long[][][].class;
				if (type == 'F')
					return float[][][].class;
				if (type == 'D')
					return double[][][].class;
				if (type == 'Z')
					return boolean[][][].class;
			} else {
				throw new ClassNotFoundException("MiscUtil cannot find primitive array classes with more that 3 dimensions");
			}

		} else {
			if (signature.contains("L"))
				return Class.forName(signature.substring(1, signature.length() - 1).replace("/", "."));
			char type = signature.charAt(0);
			if (type == 'B')
				return byte.class;
			if (type == 'S')
				return short.class;
			if (type == 'C')
				return char.class;
			if (type == 'I')
				return int.class;
			if (type == 'J')
				return long.class;
			if (type == 'F')
				return float.class;
			if (type == 'D')
				return double.class;
			if (type == 'Z')
				return boolean.class;
			if (type == 'V')
				return void.class;
		}
		return null;
	}

	private static Unsafe retriveUnsafe() {
		String disableUnsafe = System.getProperty(DISABLE_UNSAFE_ARG, "false");
		if (disableUnsafe.equals("true") || disableUnsafe.equals("t") || disableUnsafe.equals("1")) {
			System.err.println(UNSAFE_CLASS + " Is diaabled because of the vm arg " + DISABLE_UNSAFE_ARG + " was set to \"" + System.getProperty(DISABLE_UNSAFE_ARG) + "\"");
			return null;
		}
		try {
			Class<?> unsafeClass = null;
			try {
				unsafeClass = Class.forName(UNSAFE_CLASS);
			} catch (ClassNotFoundException e) {
				System.err.println("Unable to find Unsafe Class \"" + UNSAFE_CLASS + "\"");
				return null;
			}
			Field[] fields = unsafeClass.getDeclaredFields();
			for (Field field : fields) {
				try {
					field.setAccessible(true);
					Unsafe cast = (Unsafe) field.get(null);
					// System.err.println("Successfully retrived Unsafe instance. Avilable for use with MiscUtil.getUnsafe()");
					return cast;
				} catch (ClassCastException e) {
					// Ignore, there might be other static fields
				}
			}
		} catch (Exception e) {
		}
		System.err.println("Failed to retrive Unsafe instance");
		return null;
	}

	// 4MB buffer
	private static final byte[] BUFFER = new byte[4 * 1024 * 1024];

	/**
	 * copy input to output stream
	 */
	public static long copy(InputStream input, OutputStream output) throws IOException {
		int bytesRead;
		long total = 0;
		while ((bytesRead = input.read(BUFFER)) != -1) {
			output.write(BUFFER, 0, bytesRead);
			total += bytesRead;
		}
		return total;
	}

	/**
	 * Reads the desired file to a String assuming it exists
	 * 
	 * @param file The file to read
	 * @return The String representing the file
	 * @throws IOException If the file doen't not exist or if a IO error occurs while reading the file
	 */
	public static String readToString(File file) throws IOException {
		StringBuilder sb = new StringBuilder(512);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		reader.close();
		return sb.toString();
	}

	/**
	 * Reads the desired file to a byte array
	 * 
	 * @param file The file to read
	 * @return The data in the file
	 * @throws IOException If the file doen't not exist or if a IO error occurs while reading the file
	 */
	public static byte[] readToByteArray(File file) throws IOException {
		if (file == null)
			throw new NullPointerException("File can't be null!");
		byte[] data = new byte[(int) file.length()];
		FileInputStream stream = new FileInputStream(file);
		stream.read(data);
		stream.close();
		return data;
	}

	/**
	 * Reads the all the available bytes from the desired input stream
	 * 
	 * @param stream The stream to read from
	 * @return The data in the file
	 * @throws IOException If the file doen't not exist or if a IO error occurs while reading the file
	 */
	public static byte[] readToByteArray(InputStream stream) throws IOException {
		byte[] bytes = new byte[stream.available()];
		stream.read(bytes);
		return bytes;
	}

	public static String getStackTrace(Throwable error) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		error.printStackTrace(pw);
		return sw.toString();
	}

	public static String epochToString(long time) {
		return new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date(time));
	}

	public static boolean isUnsafeSupported() {
		return unsafe != null;
	}

	public static Unsafe getUnsafe() {
		if (unsafe == null)
			throw new IllegalStateException("Unsafe is not supported");
		return unsafe;
	}

	private MiscUtil() {

	}

	public static String getExtension(String name) {
		int index = name.lastIndexOf('.');
		if (index == -1)
			return name;
		return name.substring(index + 1);
	}

	/**
	 * Attempts to parse a string into an int using {@link Integer#parseInt(String, int)}. If the string cannot be parsed, the error message along
	 * with the unparsable string will be printed to {@link System#err} and the default value will be returned. Otherwise string's parsed int value
	 * will be returned
	 * 
	 * @param str The string to parse
	 * @param base the base to parse the string into
	 * @param errorMessage The error message to be printed if parsing fails
	 * @param defaultValue The default value to be returned if parsing fails
	 * @return The parsed string, or the default value if parsing fails
	 */
	public static int getIntOrDefaultValue(String str, int base, String errorMessage, int defaultValue) {
		try {
			return Integer.parseInt(str, base);
		} catch (Exception e) {
			System.err.println(errorMessage + " \"" + str + "\"");
		}
		return defaultValue;
	}

	/**
	 * Attempts to parse a string into an int using {@link Integer#parseInt(String, int)}. If the string cannot be parsed, then the runnable will be
	 * run and the default value will be returned. Otherwise string's parsed integer value will be returned
	 * 
	 * @param str The string to parse
	 * @param base the base to parse the string into
	 * @param runnable The runnable to run if parsing fails
	 * @param defaultValue The default value to be returned if parsing fails
	 * @return The parsed string, or the default value if parsing fails
	 */
	public static int getIntOrRunnableAndDefValue(String str, int base, Runnable runnable, int defaultValue) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			runnable.run();
			return defaultValue;
		}
	}

	/**
	 * Attempts to parse a string into an int using {@link Integer#parseInt(String)}. If the string cannot be parsed, the error message along with
	 * the unparsable string will be printed to {@link System#err} and the default value will be returned. Otherwise string's parsed int value will
	 * be returned.<br>
	 * This is equivalent to calling {@code MiscUtil.getIntOrDefaultValue(str, 10, errorMessage, defaultValue)}
	 * 
	 * @param str The string to parse
	 * @param errorMessage The error message to be printed if parsing fails
	 * @param defaultValue The default value to be returned if parsing fails
	 * @return The parsed string, or the default value if parsing fails
	 */
	public static int getIntOrDefaultValue(String str, String errorMessage, int defaultValue) {
		return getIntOrDefaultValue(str, 10, errorMessage, defaultValue);
	}

	/**
	 * Attempts to parse a string into an int using {@link Integer#parseInt(String, int)}. If the string cannot be parsed, then the runnable will be
	 * run and the default value will be returned. Otherwise string's parsed integer value will be returned
	 * 
	 * @param str The string to parse
	 * @param runnable A runnable to run if parsing fails
	 * @param defaultValue The default value to be returned if parsing fails
	 * @return The parsed string, or the default value if parsing fails
	 */
	public static int getIntOrRunnableAndDefValue(String str, Runnable runnable, int defaultValue) {
		return getIntOrRunnableAndDefValue(str, 10, runnable, defaultValue);
	}

	public static final java.lang.reflect.Field getDeclaredField(Class<?> root, String fieldName) throws NoSuchFieldException {
		Class<?> type = root;
		do {
			try {
				java.lang.reflect.Field field = type.getDeclaredField(fieldName);
				field.setAccessible(true);
				return field;
			} catch (NoSuchFieldException e) {
				type = type.getSuperclass();
			} catch (SecurityException e) {
				type = type.getSuperclass();
			}
		} while (type != null);
		throw new NoSuchFieldException(fieldName + " does not exist in " + root.getName() + " or any of its superclasses.");
	}

	public static final long address(Buffer buffer) {
		if (!buffer.isDirect() || unsafe == null) {
			throw new UnsupportedOperationException("Unable to get Nio Buffer address! " + (unsafe == null ? "Unsafe is not supported" : "Buffer is not direct!"));
		}
		return unsafe.getLong(buffer, NIO_BUFFER_ADDRESS_OFFSET);
	}

	private static long findBufferAddress() {
		try {
			return unsafe.objectFieldOffset(getDeclaredField(Buffer.class, "address"));
		} catch (Exception e) {
			throw new UnsupportedOperationException("Could not detect ByteBuffer.address offset", e);
		}
	}

	public static void writeByteArray(File file, byte[] bytes) throws IOException {
		FileOutputStream stream = new FileOutputStream(file);
		stream.write(bytes);
		stream.close();
	}

	public static <T> T newInstanceUsingAnyMeans(Class<T> type) throws RuntimeException {
		if (isUnsafeSupported()) {
			try {
				return (T) getUnsafe().allocateInstance(type);
			} catch (InstantiationException e) {
				throw new RuntimeException("Unable to create instance of " + type, e);
			}
		} else {
			try {
				return type.newInstance();
			} catch (Exception e) {
				try {
					return MiscUtil.newInstanceUsingAConstructor(type);
				} catch (Exception e2) {
					throw new RuntimeException("All instantition stratgies failed...\nError 1:\n" + MiscUtil.getStackTrace(e) + "\n\nError2:\n" + MiscUtil.getStackTrace(e2));
				}
			}
		}
	}

	/**
	 * Creates a new instance of the specified constructor by invoking a constructor with garbage arguments
	 * 
	 * @param clazz The class to instantiate
	 * @return A new instance of class
	 * @throws RuntimeException If the class cannot be instantiated
	 */
	public static <T> T newInstanceUsingAConstructor(Class<T> clazz) throws RuntimeException {
		try {
			Constructor<T> bestOne = null;
			for (Constructor<?> c : clazz.getDeclaredConstructors()) {
				if (bestOne == null || c.getParameterTypes().length < c.getParameterTypes().length) {
					bestOne = (Constructor<T>) c;
				}
			}
			List<Object> args = new ArrayList<Object>();
			for (Class<?> type : bestOne.getParameterTypes()) {
				if (type.isPrimitive()) {
					Object obj;
					if (type == byte.class) {
						obj = Byte.valueOf((byte) 0);
					} else if (type == short.class) {
						obj = Short.valueOf((short) 0);
					} else if (type == char.class) {
						obj = Character.valueOf((char) 0);
					} else if (type == int.class) {
						obj = Integer.valueOf(0);
					} else if (type == long.class) {
						obj = Long.valueOf(0L);
					} else if (type == float.class) {
						obj = Float.valueOf(0.0f);
					} else if (type == double.class) {
						obj = Double.valueOf(0.0);
					} else if (type == boolean.class) {
						obj = Boolean.FALSE;
					} else {
						throw new RuntimeException("Unknown primative type " + type + "!!!!!!!!!!!");
					}
					args.add(obj);
				} else {
					args.add(null);
				}
			}
			return bestOne.newInstance(args.toArray());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Calls a method with default values for reach type (0, false, null, etc.)
	 * 
	 * @param method The method to call
	 * @param instance The instance to invoke the method on
	 * @return The value returned from the method
	 * @throws RuntimeException If an exception is thrown
	 */
	public static Object callMethod(Method method, Object instance) throws RuntimeException {
		try {
			List<Object> args = new ArrayList<Object>();
			for (Class<?> type : method.getParameterTypes()) {
				if (type.isPrimitive()) {
					Object obj;
					if (type == byte.class) {
						obj = Byte.valueOf((byte) 0);
					} else if (type == short.class) {
						obj = Short.valueOf((short) 0);
					} else if (type == char.class) {
						obj = Character.valueOf((char) 0);
					} else if (type == int.class) {
						obj = Integer.valueOf(0);
					} else if (type == long.class) {
						obj = Long.valueOf(0L);
					} else if (type == float.class) {
						obj = Float.valueOf(0.0f);
					} else if (type == double.class) {
						obj = Double.valueOf(0.0);
					} else if (type == boolean.class) {
						obj = Boolean.FALSE;
					} else {
						throw new RuntimeException("Unknown primative type " + type + "!!!!!!!!!!!");
					}
					args.add(obj);
				} else {
					args.add(null);
				}
			}
			return method.invoke(instance, args.toArray());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public static void pause(long nanos) throws RuntimeException {
		nanos += System.nanoTime();
		while (System.nanoTime() < nanos) {

		}
	}

	public static void pauseLarge(long nanos, long seconds) throws RuntimeException {
		try {
			Thread.sleep(Math.max((seconds * 1000L) - 1L, 0L));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		pause(nanos);
	}

	public static boolean isWrapperClass(Class<?> clazz) {
		//format:off
		return     clazz == Byte.class      || clazz == Short.class
				|| clazz == Character.class || clazz == Integer.class
				|| clazz == Long.class      || clazz == Float.class
				|| clazz == Double.class    || clazz == Boolean.class;
	}

	public static char getPrimitiveClassSignature(Class<?> clazz) {
		       if (clazz == Byte.class || clazz == byte.class) {
			return 'B';
		} else if (clazz == Short.class || clazz == short.class) {
			return 'S';
		} else if (clazz == Integer.class || clazz == int.class) {
			return 'I';
		} else if (clazz == Long.class || clazz == long.class) {
			return 'J';
		} else if (clazz == Float.class || clazz == float.class) {
			return 'F';
		} else if (clazz == Double.class || clazz == double.class) {
			return 'D';
		} else if (clazz == Character.class || clazz == char.class) {
			return 'C';
		} else if (clazz == Boolean.class || clazz == boolean.class) {
			return 'Z';
		} else if (clazz == Void.class || clazz == void.class) {
			return 'V';
		} else if (clazz == String.class) {
			return 'R';
		} else {
			throw new IllegalArgumentException(clazz + " is not primative");
		}
		//format:on
	}

	public static char[] getCharsFast(String str) {
		if (str.length() > 1000) {
			try {
				return (char[]) STRING_VALUE.get(str);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return str.toCharArray();
	}

	public static String createString(char[] chars) {
		try {
			return STRING_CHAR_CONSTRUCTOR.newInstance(chars, true);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public static int sizeof(Class dataType) {
		if (dataType == null) {
			throw new NullPointerException();
		}
		if (dataType == byte.class || dataType == Byte.class) {
			return Byte.BYTES;
		}
		if (dataType == short.class || dataType == Short.class) {
			return Short.BYTES;
		}
		if (dataType == char.class || dataType == Character.class) {
			return Character.BYTES;
		}
		if (dataType == int.class || dataType == Integer.class) {
			return Integer.BYTES;
		}
		if (dataType == long.class || dataType == Long.class) {
			return Long.BYTES;
		}
		if (dataType == float.class || dataType == Float.class) {
			return Float.BYTES;
		}
		if (dataType == double.class || dataType == Double.class) {
			return Double.BYTES;
		}
		return unsafe.addressSize();
	}

	public static Field[] getAllNonTransientFields(Class<?> type) {
		Objects.requireNonNull(type);

		ArrayList<Field> fields = new ArrayList<Field>();
		if (type.isPrimitive() || type.isArray())
			throw new IllegalArgumentException(type + " must be a user defined type!");
		while (type != Object.class) {
			for (Field field : type.getDeclaredFields()) {
				if (Modifier.isTransient(field.getModifiers()) || Modifier.isStatic(field.getModifiers()))
					continue;// Skip - we don't care

				fields.add(field);
			}
			type = type.getSuperclass();
		}
		Field[] result = new Field[fields.size()];
		fields.toArray(result);
		return result;
	}

	public static String getTimeDifference(LocalDateTime first, LocalDateTime second) {
		Objects.requireNonNull(first);
		Objects.requireNonNull(second);
		Period p;
		// System.out.println("first " + first.toString(DateTimeFormat.mediumDateTime()));
		// System.out.println("second " + second.toString(DateTimeFormat.mediumDateTime()));
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		if (first.isBefore(second)) {
			p = new Period(first, second);
		} else {
			p = new Period(second, first);
		}
		//format:off
		p = p.withSeconds(0);//no seconds or millis
		p = p.withMillis(0);
		return p.toString(PeriodFormat.getDefault());
	}


	public static String toProperEnglishName(String javaName) {
		if (javaName.toUpperCase().equals(javaName)) {// All letters are uppercase. Its the name of a constant
			char[] result = new char[javaName.length()];
			boolean lastWasSpace = true;// True to capitalize the first one
			for (int i = 0; i < result.length; i++) {
				char current = javaName.charAt(i);
				if (Character.isLetter(current)) {
					result[i] = lastWasSpace ? current : Character.toLowerCase(current);
					lastWasSpace = false;
				} else if (current == '_') {
					result[i] = ' ';
					lastWasSpace = true;
				} else {// Must be something else. Maybe a digit?
					result[i] = current;// So just assign it
					lastWasSpace = false;
				}
			}
			return new String(result);
		} else {// probably camel case
			if (javaName.indexOf(' ') != -1) {// bad... Can't be camel case because of spaces
				return javaName;// Wrong
			}
			StringBuilder sb = new StringBuilder(javaName.length());
			for (int i = 0; i < javaName.length(); i++) {
				char current = javaName.charAt(i);
				if (Character.isLetter(current)) {
					if (Character.isUpperCase(current) || (i == 0)) {// || first to make sure the first letter is capitalized
						if (i != 0)
							sb.append(' ');
						sb.append(Character.toUpperCase(current));
					} else {
						sb.append(current);
					}
				} else {// Must be something else. Maybe a digit?
					sb.append(current);// So just assign it
				}
			}

			return sb.toString();
		}
	}

}
