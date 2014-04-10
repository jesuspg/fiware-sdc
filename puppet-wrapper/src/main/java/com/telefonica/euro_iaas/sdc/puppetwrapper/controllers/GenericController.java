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
