package com.f1project.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public String handleResourceNotFound(ResourceNotFoundException exception, Model model) {
		model.addAttribute("error", exception.getMessage());
		
		return "error/404";
	}
	
	@ExceptionHandler(DuplicateResourceException.class)
	public String handleDuplicateResourceException(DuplicateResourceException exception, Model model) {
		model.addAttribute("error", exception.getMessage());
		
		return "error/409";
	}
}