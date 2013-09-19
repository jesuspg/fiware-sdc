package com.telefonica.euro_iaas.sdc.model.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * workaround to receive collections of releaseDto objects by rest requests.
 * 
 * @author Sergio Arroyo
 * 
 */
@SuppressWarnings("serial")
@XmlRootElement
@XmlSeeAlso( { ReleaseDto.class })
public class Releases extends ArrayList<ReleaseDto> {

	@XmlElement(name = "release")
	public List<ReleaseDto> getReleases() {
		return this;
	}
}
