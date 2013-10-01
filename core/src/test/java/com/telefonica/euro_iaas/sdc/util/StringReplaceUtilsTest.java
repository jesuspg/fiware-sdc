/**
 *   (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights
 *   Reserved.
 * 
 *   The copyright to the software program(s) is property of Telefonica I+D.
 *   The program(s) may be used and or copied only with the express written
 *   consent of Telefonica I+D or in accordance with the terms and conditions
 *   stipulated in the agreement/contract under which the program(s) have
 *   been supplied.
 */

package com.telefonica.euro_iaas.sdc.util;

import java.util.HashMap;

import junit.framework.Assert;

import org.junit.Test;

/**
 * StringReplaceUtils test suite.
 * 
 * @author Sergio Arroyo
 */
public class StringReplaceUtilsTest {

    public static final String TO_REPLACE = "Hi <key1> this is a <key2> called <key1>.<key2> ";

    public static final String REPLACED = "Hi Perry this is a test called Perry.test ";

    @Test
    public void testReplaceStrings() {
        HashMap<String, String> arguments = new HashMap<String, String>();
        arguments.put("key1", "Perry");
        arguments.put("key2", "test");
        arguments.put("key3", "Nothing");

        Assert.assertEquals(REPLACED, StringReplaceUtils.replace(TO_REPLACE, arguments));
    }

}
