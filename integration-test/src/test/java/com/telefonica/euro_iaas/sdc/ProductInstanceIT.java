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

package com.telefonica.euro_iaas.sdc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.sdc.client.SDCClient;
import com.telefonica.euro_iaas.sdc.client.services.ProductInstanceService;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.dto.ProductInstanceDto;
import com.telefonica.euro_iaas.sdc.model.dto.ReleaseDto;
import com.telefonica.euro_iaas.sdc.model.dto.VM;

public class ProductInstanceIT {

    SDCClient client;
    String baseUrl;
    String mediaType;
    
    public static String TOKEN ="token";
    public static String TENANT ="vdc";

    @Before
    public void setUp() {
        client = new SDCClient();
        baseUrl = "http://localhost:8888/sdc/rest";
        mediaType = "application/xml";
    }

    @Test
    public void shouldInstallProductInstance() {
        // given

        ProductInstanceService productInstanceService = client.getProductInstanceService(baseUrl, mediaType);

        String vdc = "vdc_test";
        ProductInstanceDto productInstanceDto = new ProductInstanceDto();
        ReleaseDto releaseDto = new ReleaseDto();
        releaseDto.setName("tomcat");
        releaseDto.setVersion("6");
        productInstanceDto.setProduct(releaseDto);
        VM vm = new VM();
        vm.setHostname("hostname");
        vm.setDomain("domain");
        productInstanceDto.setVm(vm);

        String callback = "http://localhost";
        // when

        Task task = productInstanceService.install(vdc, productInstanceDto, callback, TOKEN);

        // then
        assertNotNull(task);
        assertEquals("Install product tomcat in  VM hostnamedomain", task.getDescription());
        assertEquals("RUNNING", task.getStatus().name());
    }

    @Test
    public void shouldReturnEmptyListWithFindAllWithoutCriterias() {
        // given
        ProductInstanceService productInstanceService = client.getProductInstanceService(baseUrl, mediaType);
        ProductInstanceDto productInstanceDto = new ProductInstanceDto();
        ReleaseDto releaseDto = new ReleaseDto();
        releaseDto.setName("tomcat");
        releaseDto.setVersion("6");
        productInstanceDto.setProduct(releaseDto);
        VM vm = new VM();

        vm.setHostname("hostname");
        vm.setDomain("domain");
        productInstanceDto.setVm(vm);

        // when
        List<ProductInstance> productInstances = productInstanceService.findAll(null, null, null, null, null, null,
                null, null, null, null, null, TOKEN);

        // then
        assertNotNull(productInstances);
        assertTrue(productInstances.isEmpty());

    }
}
