package com.telefonica.euro_iaas.sdc.puppetwrapper.data;

import com.telefonica.euro_iaas.sdc.puppetwrapper.constants.Action;

public class Software {
	
	private String eol = System.getProperty("line.separator");
	
	private String name;
	private String version;
	private Action action;
	
	public Software() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	/**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */
	public String toString()
	{
	    final String TAB = "    ";
	    
	    String retValue = "";
	    
	    retValue = "Software ( "
	        + super.toString() + TAB
	        + "name = " + this.name + TAB
	        + "version = " + this.version + TAB
	        + "action = " + this.action + TAB
	        + " )";
	
	    return retValue;
	}

	public String generateFileStr() {
		StringBuffer sb =new StringBuffer();
		sb.append("class{'"+this.name+"::"+action.getActionString(action.getCode())+"':}");
		sb.append(eol);
		
		return sb.toString();

	}
	
	

}
