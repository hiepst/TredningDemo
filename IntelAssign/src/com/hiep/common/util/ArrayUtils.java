package com.hiep.common.util;


/**
 * Copyright 2002-2004 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import java.lang.reflect.Array;

/**
 * Static utility functions to assist in working with arrays. Aim is to reduce
 * repeat code.
 *
 * @author Keith Donald, adapted from jakarta-commons-lang's ArrayUtils
 */
public class ArrayUtils {

	/** Immutable empty object array */
	public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

	private ArrayUtils() {
	}

	/**
	 * Checks if an array of Objects has elements.
	 *
	 * @param array
	 *            the array to test
	 * @return <code>true</code> if the array has elements, false is the array
	 *         is empty or <code>null</code>
	 */
	public static boolean hasElements(final Object[] array) {
		return !(array == null || array.length == 0);
	}

	/**
	 * Convert a primitive array to an object array of wrapper objects.
	 *
	 * @param primitiveArray
	 *            The primitive array
	 * @return The object array.
	 * @throws IllegalArgumentException
	 *             if the parameter is not a primitive array.
	 */
	public static Object[] toObjectArrayFromPrimitive(Object primitiveArray) {
		// if null, return
		if (primitiveArray == null) {
			return EMPTY_OBJECT_ARRAY;
		}
		// if not an array or elements not primitive, illegal argument...
		Class clazz = primitiveArray.getClass();
		Assert.isTrue(clazz.isArray(), "The specified parameter is not an array.");
		Assert.isTrue(clazz.getComponentType().isPrimitive(), "The specified parameter is not a primitive array.");

		// get array length and create Object output array
		int length = Array.getLength(primitiveArray);
		Object[] newArray = new Object[length];
		// wrap and copy elements
		for (int i = 0; i < length; i++) {
			newArray[i] = Array.get(primitiveArray, i);
		}
		return newArray;
	}

	/**
	 * Ensures that an access of array[index] will function without throwing an
	 * ArrayIndexOutOfBoundsException.
	 *
	 * If array.length is 0, a new array of length 1 will be returned. If
	 * <code>index &gte; array.length</code>, a new array will be returned of
	 * length (index + 1), with the original values already populated. This
	 * allows a developer to do:
	 * 
	 * <pre>
	 * intArray = ensureLength(intArray, index);
	 * </pre>
	 *
	 * @param array
	 *            - the array to ensure length
	 * @param index
	 *            - the index location that must be accessed without a
	 *            ArrayIndexOutOfBoundsException being thrown
	 * @return a new array of length index + 1, fully populated with the
	 *         original array's values in their original positions.
	 */
	public static int[] ensureLength(int[] array, int index) {

		if (array == null) {
			array = new int[index + 1];
			return array;
		}

		if (index >= array.length) {
			int[] newArray = new int[index + 1];
			System.arraycopy(array, 0, newArray, 0, array.length);
			return newArray;
		} else {
			return array;
		}
	}

	/**
	 * Ensures that an access of array[index] will function without throwing an
	 * ArrayIndexOutOfBoundsException.
	 *
	 * If array.length is 0, a new array of length 1 will be returned. If
	 * <code>index &gte; array.length</code>, a new array will be returned of
	 * length (index + 1), with the original values already populated. This
	 * allows a developer to do:
	 * 
	 * <pre>
	 * strArray = ensureLength(strArray, index);
	 * </pre>
	 *
	 * @param array
	 *            - the array to ensure length
	 * @param index
	 *            - the index location that must be accessed without a
	 *            ArrayIndexOutOfBoundsException being thrown
	 * @return a new array of length index + 1, fully populated with the
	 *         original array's values in their original positions.
	 */
	public static String[] ensureLength(String[] array, int index) {

		if (array == null) {
			array = new String[index + 1];
			return array;
		}

		if (index >= array.length) {
			String[] newArray = new String[index + 1];
			System.arraycopy(array, 0, newArray, 0, array.length);
			return newArray;
		} else {
			return array;
		}
	}

	@SuppressWarnings("unchecked")
	public static int compare(Comparable[] array1, Comparable[] array2) {

		if (array1 == null) {
			return array2 == null ? 0 : -1;
		}

		if (array2 == null) {
			return 1;
		}

		int count = Math.min(array1.length, array2.length);
		int diff;
		for (int i = 0; i < count; i++) {

			diff = array1[i].compareTo(array2[i]);

			if (diff != 0) {
				return diff;
			}

		}

		if (array1.length == array2.length) {
			return 0;
		}

		return array1.length - array2.length;

	}

}
