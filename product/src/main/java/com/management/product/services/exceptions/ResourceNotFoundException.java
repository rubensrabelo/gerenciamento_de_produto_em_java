package com.management.product.services.exceptions;

public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ResourceNotFoundException(String type, Long id) {
		super(type + " with id=" + id + " not found.");
	}
}
