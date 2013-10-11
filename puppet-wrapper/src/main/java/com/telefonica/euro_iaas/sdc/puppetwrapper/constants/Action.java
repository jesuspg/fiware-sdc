package com.telefonica.euro_iaas.sdc.puppetwrapper.constants;

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
