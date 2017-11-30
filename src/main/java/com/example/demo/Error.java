package com.example.demo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Error {
	
	int status;
	
	String error;
	
	String message;
	
	String path;

}
