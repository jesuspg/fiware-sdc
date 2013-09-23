package com.telefonica.euro_iaas.sdc.util;

import java.util.HashMap;

import org.junit.Test;

import junit.framework.Assert;

/**
 * StringReplaceUtils test suite.
 * @author Sergio Arroyo
 *
 */
public class StringReplaceUtilsTest {

    public static final String TO_REPLACE =
        "Hi <key1> this is a <key2> called <key1>.<key2> ";

    public static final String REPLACED =
        "Hi Perry this is a test called Perry.test ";

    @Test
    public void testReplaceStrings() {
        HashMap<String, String> arguments = new HashMap<String, String>();
        arguments.put("key1", "Perry");
        arguments.put("key2", "test");
        arguments.put("key3", "Nothing");

        Assert.assertEquals(REPLACED, StringReplaceUtils.replace(
                TO_REPLACE, arguments));
    }

}
