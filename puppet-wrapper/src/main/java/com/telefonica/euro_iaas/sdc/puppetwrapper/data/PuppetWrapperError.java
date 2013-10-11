package com.telefonica.euro_iaas.sdc.puppetwrapper.data;

public class PuppetWrapperError {
    private short code;
    private String description;
    
    public static final short PUPPETWRAPPER_INTERNAL_SERVER_ERROR = -500;
    public static final short PUPPETWRAPPER_ELEMENT_NOT_FOUND = -404;
    

    /**
     * 
     */
    public PuppetWrapperError() {
        super();
    }

    /**
     * @param code
     * @param description
     */
    public PuppetWrapperError(short code, String description) {
        super();
        this.code = code;
        this.description = description;
    }

    /**
     * @return the code
     */
    public short getCode() {
        return code;
    }

    /**
     * @param code
     *            the code to set
     */
    public void setCode(short code) {
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
     * Constructs a <code>String</code> with all attributes in name = value
     * format.
     * 
     * @return a <code>String</code> representation of this object.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("[[Error]");
        sb.append("[code = ").append(this.code).append("]");
        sb.append("[description = ").append(this.description).append("]");
        sb.append("]");
        return sb.toString();
    }

}

