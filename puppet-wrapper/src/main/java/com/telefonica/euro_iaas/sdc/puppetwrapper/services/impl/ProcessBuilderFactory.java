package com.telefonica.euro_iaas.sdc.puppetwrapper.services.impl;

import java.io.IOException;
import java.util.List;

public class ProcessBuilderFactory {
    
    public Process createProcessBuilder(String... command) throws IOException{
        
        ProcessBuilder pb= new ProcessBuilder(command);
        
        return pb.start();
    }

}
