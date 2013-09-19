package com.telefonica.euro_iaas.sdc.rest.validation;

import com.sun.jersey.multipart.MultiPart;
import com.telefonica.euro_iaas.sdc.exception.InvalidMultiPartRequestException;

public class MultipartValidator {

	public void validateMultipart(MultiPart multiPart, Class clase)
			throws InvalidMultiPartRequestException {

		// Checking that multipart object is not null
		if (multiPart == null)
			throw new InvalidMultiPartRequestException("The "
					+ "MultiPart object is null");

		// Checking thet multipart is composed of three objects
		if (multiPart.getBodyParts().size() != 3)
			throw new InvalidMultiPartRequestException("The "
					+ "MultiPart object should be composed of three parts");

		try {
			multiPart.getBodyParts().get(0).getEntityAs(clase);
		} catch (IllegalStateException e) {
			throw new InvalidMultiPartRequestException("First "
					+ " entity of MultiPart is not a class type " + clase);
		} catch (IllegalArgumentException e) {
			throw new InvalidMultiPartRequestException("First "
					+ " entity of MultiPart is not a class type " + clase);
		}

		/*
		 * if
		 * (!multiPart.getBodyParts().get(2).getEntity().getClass().equals(byte
		 * [].class)) throw new InvalidMultiPartRequestException(" Third " +
		 * " entity of MultiPart should be of type " + byte[].class);
		 */

		try {
			multiPart.getBodyParts().get(1).getEntityAs(byte[].class);
		} catch (IllegalStateException e) {
			throw new InvalidMultiPartRequestException("Second "
					+ " entity of MultiPart is not a class type "
					+ byte[].class);
		} catch (IllegalArgumentException e) {
			throw new InvalidMultiPartRequestException("Second "
					+ " entity of MultiPart is not a class type " + clase);
		}

		try {
			multiPart.getBodyParts().get(2).getEntityAs(byte[].class);
		} catch (IllegalStateException e) {
			throw new InvalidMultiPartRequestException("Third "
					+ " entity of MultiPart is not a class type "
					+ byte[].class);
		} catch (IllegalArgumentException e) {
			throw new InvalidMultiPartRequestException("Third "
					+ " entity of MultiPart is not a class type " + clase);
		}

	}
}
