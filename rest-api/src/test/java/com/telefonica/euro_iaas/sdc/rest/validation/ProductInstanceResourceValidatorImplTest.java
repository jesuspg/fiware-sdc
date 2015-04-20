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

/**
 *
 */
package com.telefonica.euro_iaas.sdc.rest.validation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.fiware.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.exception.InvalidProductException;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.dto.ProductInstanceDto;
import com.telefonica.euro_iaas.sdc.model.dto.ReleaseDto;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;

/**
 * @author jesus.movilla
 */
public class ProductInstanceResourceValidatorImplTest {

    private String serversResponse;
    private String serverResponse;
    private ProductInstanceResourceValidatorImpl validator;
    private GeneralResourceValidator generalValidator;

    @Before
    public void setup() {

        validator = new ProductInstanceResourceValidatorImpl();
        generalValidator = new GeneralResourceValidatorImpl();
        validator.setGeneralValidator(generalValidator);
        serversResponse = "{\"servers\": "
                + "["
                + "{"
                + "\"id\": \"2e855d51-4593-41de-8239-48200859d30b\", "
                + "\"links\": ["
                + "{\"href\": \"http://130.206.80.63:8774/v2/ebe6d9ec7b024361b7a3882c65a57dda/servers/2e855d51-4593-41de-8239-48200859d30b\", \"rel\": \"self\"}, "
                + "{\"href\": \"http://130.206.80.63:8774/ebe6d9ec7b024361b7a3882c65a57dda/servers/2e855d51-4593-41de-8239-48200859d30b\", \"rel\": \"bookmark\"}], "
                + "\"name\": \"31072013-01-bp-31072013-01test-1\""
                + "}, "
                + "{"
                + "\"id\": \"4df2d4ff-233d-42ca-b7fb-c2d7787f1ee5\", "
                + "\"links\": ["
                + "{\"href\": \"http://130.206.80.63:8774/v2/ebe6d9ec7b024361b7a3882c65a57dda/servers/4df2d4ff-233d-42ca-b7fb-c2d7787f1ee5\", \"rel\": \"self\"}, "
                + "{\"href\": \"http://130.206.80.63:8774/ebe6d9ec7b024361b7a3882c65a57dda/servers/4df2d4ff-233d-42ca-b7fb-c2d7787f1ee5\", \"rel\": \"bookmark\"}], "
                + "\"name\": \"22072013-11-bp-22072013-11test-1\"" + "}" + "]" + "}";

        serverResponse = "{\"server\": "
                + "{\"OS-EXT-STS:task_state\": \"active\", "
                + "\"addresses\": {\"private\": [{\"version\": 4, \"addr\": \"172.30.5.4\"}, {\"version\": 4, \"addr\": \"130.206.82.66\"}]}, "
                + "\"links\": ["
                + "	{\"href\": \"http://130.206.80.63:8774/v2/ebe6d9ec7b024361b7a3882c65a57dda/servers/2e855d51-4593-41de-8239-48200859d30b\", \"rel\": \"self\"}, "
                + "	{\"href\": \"http://130.206.80.63:8774/ebe6d9ec7b024361b7a3882c65a57dda/servers/2e855d51-4593-41de-8239-48200859d30b\", \"rel\": \"bookmark\"}], "
                + "\"image\": {\"id\": \"44dcdba3-a75d-46a3-b209-5e9035d2435e\", "
                + "\"links\": [{\"href\": \"http://130.206.80.63:8774/ebe6d9ec7b024361b7a3882c65a57dda/images/44dcdba3-a75d-46a3-b209-5e9035d2435e\", \"rel\": \"bookmark\"}]}, "
                + "\"OS-EXT-STS:vm_state\": \"active\", \"flavor\": {\"id\": \"2\", \"links\": [{\"href\": \"http://130.206.80.63:8774/ebe6d9ec7b024361b7a3882c65a57dda/flavors/2\", \"rel\": \"bookmark\"}]}, "
                + "\"id\": \"2e855d51-4593-41de-8239-48200859d30b\", \"user_id\": \"e07b345d64c24c068726988617718792\", \"OS-DCF:diskConfig\": \"MANUAL\", \"accessIPv4\": \"\", \"accessIPv6\": \"\", "
                + "\"progress\": 0, \"OS-EXT-STS:power_state\": 1, \"config_drive\": \"\", \"status\": \"ACTIVE\", \"updated\": \"2013-07-31T13:22:18Z\", \"hostId\": \"46dba47bce43aa8207ccf45cee7ea3888903141554807cd3d4cd6c5a\", "
                + "\"key_name\": \"jesusmovilla\", \"name\": \"31072013-01-bp-31072013-01test-1\", \"created\": \"2013-07-31T13:21:43Z\", \"tenant_id\": \"ebe6d9ec7b024361b7a3882c65a57dda\", \"metadata\": {}}}";

    }

    @Test
    public void testGetServerIds() throws Exception {

        List<String> serverids = validator.getServerIds(serversResponse);
        assertEquals(2, serverids.size());
        assertEquals("2e855d51-4593-41de-8239-48200859d30b", serverids.get(0));
        assertEquals("4df2d4ff-233d-42ca-b7fb-c2d7787f1ee5", serverids.get(1));
    }

    @Test
    public void testGetServerIP() throws Exception {

        String ip = validator.getServerPublicIP(serverResponse);
        assertEquals("130.206.82.66", ip);
    }

    @Test(expected = InvalidProductException.class)
    public void testTestProductIsNull() throws Exception {
        ProductInstanceDto product = new ProductInstanceDto();
        validator.validateInsert(product);

    }

    @Test(expected = InvalidProductException.class)
    public void testTestProductNameisEmpty() throws Exception {
        ProductInstanceDto product = new ProductInstanceDto();
        ReleaseDto p = new ReleaseDto("", "de", "1.0");
        product.setProduct(p);
        validator.validateInsert(product);

    }

    @Test(expected = InvalidProductException.class)
    public void testTestProductAttributeBadType() throws Exception {
        ProductInstanceDto product = new ProductInstanceDto();
        ReleaseDto p = new ReleaseDto("test", "de", "1.0");
        product.setProduct(p);
        List<Attribute> attributes = new ArrayList<Attribute>();
        attributes.add(new Attribute("key", "value", "description", "type"));
        product.setAttributes(attributes);
        validator.validateInsert(product);

    }

    @Test
    public void testTestProductAttributeOK() throws Exception {
        ProductInstanceDto product = new ProductInstanceDto();
        ReleaseDto p = new ReleaseDto("test", "de", "1.0");
        product.setProduct(p);
        VM vm = new VM("fqn","1.1.1.1","hostname","domain","osType");
        product.setVm(vm);
        List<Attribute> attributes = new ArrayList<Attribute>();
        attributes.add(new Attribute("key", "value", "description", "IP"));
        product.setAttributes(attributes);
        SystemPropertiesProvider systemPropertiesProvider= mock(SystemPropertiesProvider.class);
        validator.setSystemPropertiesProvider(systemPropertiesProvider);
        when(systemPropertiesProvider.getProperty(anyString())).thenReturn("Plain|IP|IPALL");
        ProductResourceValidator productResourceValidator=mock(ProductResourceValidator.class);
        validator.setProductResourceValidator(productResourceValidator);
        validator.validateInsert(product);

    }
    
    @Test(expected=InvalidProductException.class)
    public void testTestProductAttributeNoType() throws Exception {
        ProductInstanceDto product = new ProductInstanceDto();
        ReleaseDto p = new ReleaseDto("test", "de", "1.0");
        product.setProduct(p);
        VM vm = new VM("fqn","1.1.1.1","hostname","domain","osType");
        product.setVm(vm);
        List<Attribute> attributes = new ArrayList<Attribute>();
        attributes.add(new Attribute("key", "value", "description", ""));
        product.setAttributes(attributes);
        SystemPropertiesProvider systemPropertiesProvider= mock(SystemPropertiesProvider.class);
        validator.setSystemPropertiesProvider(systemPropertiesProvider);
        when(systemPropertiesProvider.getProperty(anyString())).thenReturn("Plain|IP|IPALL");
        ProductResourceValidator productResourceValidator=mock(ProductResourceValidator.class);
        validator.setProductResourceValidator(productResourceValidator);
        validator.validateInsert(product);

    }

}
