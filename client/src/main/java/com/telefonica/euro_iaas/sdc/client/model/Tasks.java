/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.client.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.telefonica.euro_iaas.sdc.model.Task;

@SuppressWarnings("serial")
@XmlRootElement
@XmlSeeAlso({ Task.class })
public class Tasks extends ArrayList<Task> {

    @XmlElement(name = "task")
    public List<Task> getTasks() {
        return this;
    }
}
