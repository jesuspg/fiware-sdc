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

package com.telefonica.euro_iaas.sdc.validation;

import com.telefonica.euro_iaas.sdc.exception.FSMViolationException;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Unit test for FSMValidatorImpl
 * 
 * @author Sergio Arroyo
 */
public class FSMValidatorImplTest {
    @Test
    public void testInstallingTransitions() throws Exception {
        ProductInstance instance = new ProductInstance();
        instance.setStatus(Status.INSTALLING);

        FSMValidatorImpl validator = new FSMValidatorImpl();
        // possible transitions
        validator.validate(instance, Status.INSTALLED);
        validator.validate(instance, Status.ERROR);
        // invalid transitions
        try {
            validator.validate(instance, Status.UNINSTALLING);
            TestCase.fail("FSMViolationException expected");
        } catch (FSMViolationException e) {
            // it's ok
        }
        try {
            validator.validate(instance, Status.CONFIGURING);
            TestCase.fail("FSMViolationException expected");
        } catch (FSMViolationException e) {
            // it's ok
        }
        try {
            validator.validate(instance, Status.UPGRADING);
            TestCase.fail("FSMViolationException expected");
        } catch (FSMViolationException e) {
            // it's ok
        }
        try {
            validator.validate(instance, Status.UNINSTALLED);
            TestCase.fail("FSMViolationException expected");
        } catch (FSMViolationException e) {
            // it's ok
        }
    }

    @Test
    public void testInstalledTransitions() throws Exception {
        ProductInstance instance = new ProductInstance();
        instance.setStatus(Status.INSTALLED);

        FSMValidatorImpl validator = new FSMValidatorImpl();
        // possible transitions
        validator.validate(instance, Status.UNINSTALLING);
        validator.validate(instance, Status.CONFIGURING);
        validator.validate(instance, Status.UPGRADING);
        validator.validate(instance, Status.ERROR);
        // invalid transitions
        try {
            validator.validate(instance, Status.INSTALLING);
            TestCase.fail("FSMViolationException expected");
        } catch (FSMViolationException e) {
            // it's ok
        }
        try {
            validator.validate(instance, Status.UNINSTALLED);
            TestCase.fail("FSMViolationException expected");
        } catch (FSMViolationException e) {
            // it's ok
        }
    }

    @Test
    public void testConfiguringTransitions() throws Exception {
        ProductInstance instance = new ProductInstance();
        instance.setStatus(Status.CONFIGURING);

        FSMValidatorImpl validator = new FSMValidatorImpl();
        // possible transitions
        validator.validate(instance, Status.INSTALLED);
        validator.validate(instance, Status.ERROR);
        // invalid transitions
        try {
            validator.validate(instance, Status.INSTALLING);
            TestCase.fail("FSMViolationException expected");
        } catch (FSMViolationException e) {
            // it's ok
        }
        try {
            validator.validate(instance, Status.UNINSTALLING);
            TestCase.fail("FSMViolationException expected");
        } catch (FSMViolationException e) {
            // it's ok
        }
        try {
            validator.validate(instance, Status.UNINSTALLED);
            TestCase.fail("FSMViolationException expected");
        } catch (FSMViolationException e) {
            // it's ok
        }
        try {
            validator.validate(instance, Status.UPGRADING);
            TestCase.fail("FSMViolationException expected");
        } catch (FSMViolationException e) {
            // it's ok
        }

    }

    @Test
    public void testUpgradingTransitions() throws Exception {
        ProductInstance instance = new ProductInstance();
        instance.setStatus(Status.UPGRADING);

        FSMValidatorImpl validator = new FSMValidatorImpl();
        // possible transitions
        validator.validate(instance, Status.INSTALLED);
        validator.validate(instance, Status.ERROR);
        // invalid transitions
        try {
            validator.validate(instance, Status.INSTALLING);
            TestCase.fail("FSMViolationException expected");
        } catch (FSMViolationException e) {
            // it's ok
        }
        try {
            validator.validate(instance, Status.UNINSTALLING);
            TestCase.fail("FSMViolationException expected");
        } catch (FSMViolationException e) {
            // it's ok
        }
        try {
            validator.validate(instance, Status.UNINSTALLED);
            TestCase.fail("FSMViolationException expected");
        } catch (FSMViolationException e) {
            // it's ok
        }
        try {
            validator.validate(instance, Status.CONFIGURING);
            TestCase.fail("FSMViolationException expected");
        } catch (FSMViolationException e) {
            // it's ok
        }

    }

    @Test
    public void testUninstallingTransitions() throws Exception {
        ProductInstance instance = new ProductInstance();
        instance.setStatus(Status.UNINSTALLING);

        FSMValidatorImpl validator = new FSMValidatorImpl();
        // possible transitions
        validator.validate(instance, Status.UNINSTALLED);
        validator.validate(instance, Status.ERROR);
        // invalid transitions
        try {
            validator.validate(instance, Status.INSTALLING);
            TestCase.fail("FSMViolationException expected");
        } catch (FSMViolationException e) {
            // it's ok
        }
        try {
            validator.validate(instance, Status.CONFIGURING);
            TestCase.fail("FSMViolationException expected");
        } catch (FSMViolationException e) {
            // it's ok
        }
        try {
            validator.validate(instance, Status.INSTALLED);
            TestCase.fail("FSMViolationException expected");
        } catch (FSMViolationException e) {
            // it's ok
        }
        try {
            validator.validate(instance, Status.UPGRADING);
            TestCase.fail("FSMViolationException expected");
        } catch (FSMViolationException e) {
            // it's ok
        }

    }

    @Test
    public void testUninstalledTransitions() throws Exception {
        ProductInstance instance = new ProductInstance();
        instance.setStatus(Status.UNINSTALLED);

        FSMValidatorImpl validator = new FSMValidatorImpl();
        // possible transitions
        validator.validate(instance, Status.ERROR);
        validator.validate(instance, Status.INSTALLING);
        // invalid transitions
        try {
            validator.validate(instance, Status.CONFIGURING);
            TestCase.fail("FSMViolationException expected");
        } catch (FSMViolationException e) {
            // it's ok
        }
        try {
            validator.validate(instance, Status.INSTALLED);
            TestCase.fail("FSMViolationException expected");
        } catch (FSMViolationException e) {
            // it's ok
        }
        try {
            validator.validate(instance, Status.UPGRADING);
            TestCase.fail("FSMViolationException expected");
        } catch (FSMViolationException e) {
            // it's ok
        }
        try {
            validator.validate(instance, Status.UNINSTALLING);
            TestCase.fail("FSMViolationException expected");
        } catch (FSMViolationException e) {
            // it's ok
        }

    }

    @Test
    public void testErrorTransitions() throws Exception {
        ProductInstance instance = new ProductInstance();
        instance.setStatus(Status.ERROR);

        FSMValidatorImpl validator = new FSMValidatorImpl();
        // possible transitions
        validator.validate(instance, Status.INSTALLING);
        // invalid transitions
        try {
            validator.validate(instance, Status.INSTALLED);
            TestCase.fail("FSMViolationException expected");
        } catch (FSMViolationException e) {
            // it's ok
        }
        try {
            validator.validate(instance, Status.UNINSTALLING);
            TestCase.fail("FSMViolationException expected");
        } catch (FSMViolationException e) {
            // it's ok
        }
        try {
            validator.validate(instance, Status.UNINSTALLED);
            TestCase.fail("FSMViolationException expected");
        } catch (FSMViolationException e) {
            // it's ok
        }
        try {
            validator.validate(instance, Status.UPGRADING);
            TestCase.fail("FSMViolationException expected");
        } catch (FSMViolationException e) {
            // it's ok
        }
        try {
            validator.validate(instance, Status.CONFIGURING);
            TestCase.fail("FSMViolationException expected");
        } catch (FSMViolationException e) {
            // it's ok
        }

    }

}
