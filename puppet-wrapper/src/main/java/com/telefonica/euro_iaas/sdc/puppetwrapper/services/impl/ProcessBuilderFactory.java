package com.telefonica.euro_iaas.sdc.puppetwrapper.services.impl;

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service("processBuilderFactory")
public class ProcessBuilderFactory {

    private static final Log logger = LogFactory.getLog(ProcessBuilderFactory.class);

    
    public Process createProcessBuilder(String[] command) throws IOException {

        Process p = Runtime.getRuntime().exec(command);

        return p;
    }

}
