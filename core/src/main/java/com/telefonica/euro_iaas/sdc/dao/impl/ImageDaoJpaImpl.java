package com.telefonica.euro_iaas.sdc.dao.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.ImageDao;
import com.telefonica.euro_iaas.sdc.model.Image;
/**
 * JPA implementation for Vapp.
 *
 * @author Sergio Arroyo
 * @version $Id: $
 */
public class ImageDaoJpaImpl extends AbstractBaseDao<Image, Long>
    implements ImageDao {

    /** {@inheritDoc} */
    @Override
    public List<Image> findAll() {
        return super.findAll(Image.class);
    }

    /** {@inheritDoc} */
    @Override
    public Image load(Long id) throws EntityNotFoundException {
        return super.loadByField(Image.class, "id", id);
    }


}
