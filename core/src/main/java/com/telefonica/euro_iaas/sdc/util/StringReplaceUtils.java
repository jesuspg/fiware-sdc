package com.telefonica.euro_iaas.sdc.util;

import java.util.Map;

/**
 * Provides some common utilities to work with String objects.
 * 
 * @author Sergio Arroyo
 * 
 */
public class StringReplaceUtils {

	public static final String LEFT_LIMITER = "<";
	public static final String RIGHT_LIMITER = ">";

	/**
	 * Replace the substrings that match with mask
	 * <code>&lt;argument.key&gt;</code> with <code>argument.value</code>
	 * 
	 * @param target
	 *            the string with the placeholders
	 * @param arguments
	 *            the values to replace
	 * @return the string with the updated values.
	 */
	public static String replace(String target, Map<String, String> arguments) {
		return replace(target, arguments, LEFT_LIMITER, RIGHT_LIMITER);
	}

	/**
	 * Replace the substrings that match with mask
	 * <code>"leftLimiter"argument.key"rightLimiter"</code> with
	 * <code>argument.value</code>
	 * 
	 * @param target
	 *            the string with the placeholders
	 * @param arguments
	 *            the values to replace
	 * @param leftLimiter
	 *            the string that limits the placeholder by left
	 * @param rightLimiter
	 *            the string that limits the placeholder by right
	 * @return the string with the updated values.
	 */
	public static String replace(String target, Map<String, String> arguments,
			String leftLimiter, String rightLimiter) {
		for (String key : arguments.keySet()) {
			target = target.replaceAll(leftLimiter + key + rightLimiter,
					arguments.get(key));
		}
		return target;
	}
}
