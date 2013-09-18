package com.telefonica.euro_iaas.sdc.util;

import com.telefonica.euro_iaas.sdc.model.SO;
import com.telefonica.euro_iaas.sdc.model.SOInstance;
import com.telefonica.euro_iaas.sdc.model.Image;

/**
 * <p>SODeployer interface.</p>
 *
 * @author ju
 * @version $Id: $
 */
public interface SODeployer {
    /**
     * Deploy a SO ...
     *
     * @param so a {@link com.telefonica.euro_iaas.sdc.model.SO} object.
     * @return a {@link com.telefonica.euro_iaas.sdc.model.SOInstance} object.
     */
    SOInstance deploySO(SO so);
    Image stopImage(Image image);

}
