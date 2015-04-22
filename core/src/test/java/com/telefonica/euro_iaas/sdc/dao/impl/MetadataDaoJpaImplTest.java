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
package com.telefonica.euro_iaas.sdc.dao.impl;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telefonica.fiware.commons.dao.AlreadyExistsEntityException;
import com.telefonica.fiware.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.MetadataDao;
import com.telefonica.euro_iaas.sdc.model.Metadata;

/**
 * Units test, based on hSQL, for Metadata DAO.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring-test-db-config.xml", "classpath:/spring-dao-config.xml" })
public class MetadataDaoJpaImplTest {

    @Autowired
    private MetadataDao metadataDao;

    /**
     * Test method loadById return a metadata search by Id.
     * 
     * @throws AlreadyExistsEntityException
     * @throws EntityNotFoundException
     */
    @Test
    public void shouldFindById() throws AlreadyExistsEntityException, EntityNotFoundException {
        // given
        Metadata metadata = new Metadata();
        metadata.setKey("key1");
        metadata.setValue("value1");
        metadata.setDescription("description");
        metadata = metadataDao.create(metadata);

        // when
        Metadata responseMetadata = metadataDao.loadById(metadata.getId());

        // then
        assertNotNull(responseMetadata);
        assertEquals("key1", responseMetadata.getKey());
        assertEquals("value1", responseMetadata.getValue());
        assertEquals("description", responseMetadata.getDescription());
    }

    /**
     * Test update a metadata object in database.
     * 
     * @throws AlreadyExistsEntityException
     * @throws EntityNotFoundException
     */
    @Test
    public void shouldUpdateMetadata() throws AlreadyExistsEntityException, EntityNotFoundException {
        // given
        Metadata metadata = new Metadata();
        metadata.setKey("key2");
        metadata.setValue("value2");
        metadata.setDescription("description");
        metadata = metadataDao.create(metadata);

        Metadata updatedMetadata = metadataDao.loadById(metadata.getId());
        // when
        updatedMetadata.setValue("newValue");
        Metadata responseMetadata = metadataDao.merge(updatedMetadata);

        // then
        assertNotNull(responseMetadata);
        assertEquals("key2", responseMetadata.getKey());
        assertEquals("newValue", responseMetadata.getValue());
        assertEquals("description", responseMetadata.getDescription());
    }

    /**
     * test method not implemented.
     * 
     * @throws EntityNotFoundException
     */
    @Test(expected = UnsupportedOperationException.class)
    public void shouldReturnExceptionWithLoadByString() throws EntityNotFoundException {
        // given

        // when
        metadataDao.load("anystring");

        // then
    }

    /**
     * get metadataDao.
     * 
     * @return
     */
    public MetadataDao getMetadataDao() {
        return metadataDao;
    }

    /**
     * set metadataDao
     * 
     * @param metadataDao
     */
    public void setMetadataDao(MetadataDao metadataDao) {
        this.metadataDao = metadataDao;
    }
}
