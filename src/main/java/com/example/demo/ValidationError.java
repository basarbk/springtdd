package com.example.demo;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ValidationError {
	
	Map<String, String> errors = new HashMap<>();
	
	

}
