/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.puppetwrapper.controllers;

import java.io.IOException;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.telefonica.euro_iaas.sdc.puppetwrapper.common.Error;

public class GenericController {

    @ExceptionHandler(NoSuchElementException.class)
    public ModelAndView handleNoSuchElementException(NoSuchElementException ex, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        int i = 1;
        Error error = new Error(i, ex.getMessage());
        return handleModelAndView(error);
    }

    @ExceptionHandler(IOException.class)
    public ModelAndView handleIOException(IOException ex, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        int i = 2;
        Error error = new Error(i, ex.getMessage());
        return handleModelAndView(error);
    }

    public ModelAndView handleModelAndView(Error error) {
        ModelAndView model = new ModelAndView("jsonView");
        model.addObject(error);
        return model;

    }
}
