package com.example.demo;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CarController {
	
	@Autowired
	CarService carService;
	
	@GetMapping("/cars")
	public ResponseEntity<?> getAllCars(){
		return ResponseEntity.ok(carService.getAllCars());
	}
	
	@GetMapping("/cars/{id:[0-9]+}")
	public ResponseEntity<?> getCarById(@PathVariable long id){
		return ResponseEntity.ok(carService.getCarById(id));
	}
	
	@PostMapping("/cars")
	public ResponseEntity<?> saveCar(@Valid @RequestBody Car car){
		return ResponseEntity.ok(carService.saveCar(car));
	}
	
	@PutMapping("/cars/{id:[0-9]+}")
	public ResponseEntity<?> updateCar(@PathVariable long id, @Valid @RequestBody Car car){
		return ResponseEntity.ok(carService.updateCar(id, car));
	}
	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ValidationError validationError(MethodArgumentNotValidException ex){
		ValidationError error = new ValidationError();
		ex.getBindingResult().getFieldErrors().forEach(f -> error.getErrors().put(f.getField(), f.getDefaultMessage()));
		return error;
	}
	
}
