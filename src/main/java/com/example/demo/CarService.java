package com.example.demo;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class CarService {
	
	CarDao carDao;
	
	public CarService(CarDao carDao){
		this.carDao = carDao;
	}
	
	public List<Car> getAllCars(){
		return this.carDao.findAll();
	}
	
	public List<Car> getAllCarsByBrand(String brand){
		return this.carDao.findByBrand(brand);
	}
	
	public Car getCarById(long id){
		Car car = carDao.findOne(id);
		if(car == null)
			throw new NotFoundException("Car not found");
		return car;
	}
	
	public Car saveCar(Car car){
		try {
			return this.carDao.save(car);			
		} catch(Exception e){
			throw new ConstraintException();
		}
	}

	public Car updateCar(long id, Car car){
		Car inDB = getCarById(id);
		
		inDB.setBrand(car.getBrand());
		inDB.setModel(car.getModel());
		inDB.setYear(car.getYear());
		return this.carDao.save(inDB);
	}

}
