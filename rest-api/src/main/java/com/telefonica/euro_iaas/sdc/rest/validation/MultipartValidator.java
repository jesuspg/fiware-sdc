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

package com.telefonica.euro_iaas.sdc.rest.validation;

import org.glassfish.jersey.media.multipart.MultiPart;

import com.telefonica.euro_iaas.sdc.exception.InvalidMultiPartRequestException;

public class MultipartValidator {

    public void validateMultipart(MultiPart multiPart, Class clase) throws InvalidMultiPartRequestException {

        // Checking that multipart object is not null
        if (multiPart == null)
            throw new InvalidMultiPartRequestException("The " + "MultiPart object is null");

        // Checking thet multipart is composed of three objects
        if (multiPart.getBodyParts().size() != 3)
            throw new InvalidMultiPartRequestException("The " + "MultiPart object should be composed of three parts");

        try {
            multiPart.getBodyParts().get(0).getEntityAs(clase);
        } catch (IllegalStateException e) {
            throw new InvalidMultiPartRequestException("First " + " entity of MultiPart is not a class type " + clase);
        } catch (IllegalArgumentException e) {
            throw new InvalidMultiPartRequestException("First " + " entity of MultiPart is not a class type " + clase);
        }

        /*
         * if (!multiPart.getBodyParts().get(2).getEntity().getClass().equals(byte [].class)) throw new
         * InvalidMultiPartRequestException(" Third " + " entity of MultiPart should be of type " + byte[].class);
         */

        try {
            multiPart.getBodyParts().get(1).getEntityAs(byte[].class);
        } catch (IllegalStateException e) {
            throw new InvalidMultiPartRequestException("Second " + " entity of MultiPart is not a class type "
                    + byte[].class);
        } catch (IllegalArgumentException e) {
            throw new InvalidMultiPartRequestException("Second " + " entity of MultiPart is not a class type " + clase);
        }

        try {
            multiPart.getBodyParts().get(2).getEntityAs(byte[].class);
        } catch (IllegalStateException e) {
            throw new InvalidMultiPartRequestException("Third " + " entity of MultiPart is not a class type "
                    + byte[].class);
        } catch (IllegalArgumentException e) {
            throw new InvalidMultiPartRequestException("Third " + " entity of MultiPart is not a class type " + clase);
        }

    }
}
