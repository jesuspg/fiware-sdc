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

package com.telefonica.euro_iaas.sdc.dao;

import org.springframework.test.jpa.AbstractJpaTests;

public class AbstractJpaDaoTest extends AbstractJpaTests {

    @Override
    protected String[] getConfigLocations() {
        return new String[] { "classpath:/spring-test-db-config.xml", "classpath:/spring-dao-config.xml" };
    }

    /**
     * This test is because AbstractJpaTests needs at least one test to work.
     */
    public void testFoo() {

    }
}
