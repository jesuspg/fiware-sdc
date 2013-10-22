/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.puppetwrapper.common;

import static java.text.MessageFormat.format;

import java.util.NoSuchElementException;


public enum Action {
    INSTALL(1, "install"),
    UNINSTALL(2,"uninstall");
    
    private final int code;
    private final String description;
    
    
    Action(int code, String description){
        this.code = code;
        this.description = description;
    }
    
    public int getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public String getActionString(int id){
		
    	 for(Action action : Action.values()){
             if(action.getCode() == code){
                 return action.getDescription();
             }
    	 }
    	 throw new NoSuchElementException(format("Value not defined: [{0}]", code));
    }

    
}
