package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CarDao extends JpaRepository<Car, Long> {
	
	List<Car> findByBrand(String brand);
	
	List<Car> findByModel(String model);

}
