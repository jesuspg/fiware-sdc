package com.telefonica.euro_iaas.sdc.pupperwrapper.constants;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author alberts
 * 
 */
public class Error {
	
    private static final Log logger = LogFactory.getLog(Error.class);
	 
	private int code;
	private String description;

	public Error() {
		super();
	}

	/**
	 * @param code
	 * @param description
	 */
	public Error(int code, String description) {
		super();
		this.code = code;
		this.description = description;
		logger.error("code[" + code + "] description: " + description+"]");
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}


    /**
     * Constructs a <code>String</code> with all attributes
     * in name = value format.
     *
     * @return a <code>String</code> representation 
     * of this object.
     */
    public String toString() {
       StringBuilder sb = new StringBuilder("[[MakeawishError]");
       sb.append("[code = ").append(this.code).append("]");
       sb.append("[description = ").append(this.description).append("]");
       sb.append("]");
       return sb.toString();
    }

    
}

