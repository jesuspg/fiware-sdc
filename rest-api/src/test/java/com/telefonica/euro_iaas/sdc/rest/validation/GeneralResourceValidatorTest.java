/**
 * 
 */
package com.telefonica.euro_iaas.sdc.rest.validation;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.sdc.exception.InvalidNameException;

/**
 * @author jesus.movilla
 *
 */
public class GeneralResourceValidatorTest {

    GeneralResourceValidatorImpl generalResourceValidator;
    
    @Before
    public void setup() {
        generalResourceValidator = new GeneralResourceValidatorImpl();
    }
    
    @Test
    public void testValidateNameWhenIsNull() throws Exception {
        String name=null;
        try{
            generalResourceValidator.validateName(name);
        } catch (InvalidNameException e){}
    }
    
    @Test
    public void testValidateNameWhenIsEmpty() throws Exception {
        String name="";
        try{
            generalResourceValidator.validateName(name);
        } catch (InvalidNameException e){}
    }
    
    @Test
    public void testValidateNameWhenIsLOngerThan256Characters() throws Exception {
        String name=
             "12345678901234567890123456789012345678901234567890123456789012345678901234567890" +
             "12345678901234567890123456789012345678901234567890123456789012345678901234567890" +
             "12345678901234567890123456789012345678901234567890123456789012345678901234567890" +
             "12345678901234567";
        try{
            generalResourceValidator.validateName(name);
        } catch (InvalidNameException e){}
    }
}
