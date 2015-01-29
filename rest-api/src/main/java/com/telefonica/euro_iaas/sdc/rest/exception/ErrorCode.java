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

package com.telefonica.euro_iaas.sdc.rest.exception;

public enum ErrorCode {

    DB_CONNECTION(20, "Could not open connection to database", "(.*)JDBCConnectionException(.*)", 500),
    HIBERNATE(10, "Problem in database backend", "(.*)org.hibernate(.*)", 500),
    ENTITY_NOT_FOUND(30, "Entity not found", "(.*)EntityNotFoundException(.*)", 404),
    ALREADY_EXIST(31, "Entity already exist", "(.*)AlreadyExistsEntityException(.*)", 409),
    ALREADY_EXIST2(32, "Invalid environment", "(.*)already exists(.*)", 409),
    ALREADY_EXIST3(33,"Entity already exist", "Trying to persist a duplicated java.lang.Class entity(.*)", 409),
    OPERATION_NO_IMPLEMENTED(34, "Operation not implemented", "(.*)not implemented(.*)", 501),
    
    
    ENVIRONMENT_IN_USE(40,
            "The environment is being used by an instance",
            "(.*)InvalidEntityException: (.*)is being used(.*)",
            403),
    NAME_NO_VALID(41, "The entity is not valid", "(.*)InvalidEntityException:(.*)", 400),
    INFRASTRUCTURE(50, "OpenStack infrastructure failure", "(.*)InfrastructureException(.*)", 500),
    
    INVALID_PRODUCT(60, "Invalid product", "(.*)InvalidProduct(.*)", 400),
    
    DEFAULT(500, "Internal SDC Server error", "(?s).*", 500);

    private final int code;
    private final String publicMessage;
    private final String pattern;
    private final int httpCode;

    private ErrorCode(Integer code, String publicMessage, String pattern, Integer httpCode) {
        this.code = code;
        this.publicMessage = publicMessage;
        this.pattern = pattern;
        this.httpCode = httpCode;
    }

    public String getPublicMessage() {
        return publicMessage;
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code + "(" + httpCode + "): " + publicMessage;
    }

    public static ErrorCode find(String value) {
        if (value == null) {
            return ErrorCode.DEFAULT;
        }

        ErrorCode[] errors = ErrorCode.values();
        int i = 0;
        while (i < errors.length && !value.matches(errors[i].getPattern())) {
            i++;
        }
        return errors[i];
    }

    public String getPattern() {
        return pattern;
    }

    public Integer getHttpCode() {
        return this.httpCode;
    }
}
