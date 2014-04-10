/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

package com.telefonica.euro_iaas.sdc.util;

import java.util.Map;

/**
 * Provides some common utilities to work with String objects.
 * 
 * @author Sergio Arroyo
 */
public class StringReplaceUtils {

    public static final String LEFT_LIMITER = "<";
    public static final String RIGHT_LIMITER = ">";

    /**
     * Replace the substrings that match with mask <code>&lt;argument.key&gt;</code> with <code>argument.value</code>
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
     * Replace the substrings that match with mask <code>"leftLimiter"argument.key"rightLimiter"</code> with
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
    public static String replace(String target, Map<String, String> arguments, String leftLimiter, String rightLimiter) {
        for (String key : arguments.keySet()) {
            target = target.replaceAll(leftLimiter + key + rightLimiter, arguments.get(key));
        }
        return target;
    }
}
