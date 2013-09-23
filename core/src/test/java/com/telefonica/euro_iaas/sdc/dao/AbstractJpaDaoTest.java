package com.telefonica.euro_iaas.sdc.dao;

import org.springframework.test.jpa.AbstractJpaTests;

public class AbstractJpaDaoTest extends AbstractJpaTests {

    @Override
    protected String[] getConfigLocations() {
        return new String[] { "classpath:/spring-test-db-config.xml",
                "classpath:/spring-dao-config.xml" };
    }

    /**
     * This test is because AbstractJpaTests needs at least one test to work.
     */
    public void testFoo() {

    }
}
