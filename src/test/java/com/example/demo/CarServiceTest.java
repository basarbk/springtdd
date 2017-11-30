package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class CarServiceTest {
	
	CarService carService;
	
	@MockBean
	CarDao carDao;
	
	@Before
	public void init(){
		carService = new CarService(carDao);
	}
	
	@Test
	public void shouldReturnEmptyListForAllCars(){
		List<Car> cars = carService.getAllCars();
		assertThat(cars.size()).isEqualTo(0);
	}
	
	@Test(expected=NotFoundException.class)
	public void shouldReturnNotFoundException(){
		carService.getCarById(1L);
	}
	
	@Test
	public void shouldReturnCarWithValidID(){
		Mockito.when(carDao.findOne(1L)).thenReturn(new Car(1L, "tesla","models",111));
		Car car = carService.getCarById(1L);
		assertThat(car).isNotNull();
		assertThat(car.getBrand()).isEqualTo("tesla");
	}

	@Test(expected=NotFoundException.class)
	public void shouldReturnNotFoundExceptionForUpdatingUnknownCar(){
		carService.updateCar(1L, new Car(1L, "tesla","models",111));
	}
	
	@Test
	public void shouldUpdateCarWithValidID(){
		Mockito.when(carDao.findOne(1L)).thenReturn(new Car(1L, "tesla","models",111));
		Car car = new Car(1L, "tesla", "modelx", 111);
		Mockito.when(carDao.save(car)).thenReturn(car);
		Car response = carService.updateCar(1L, car);
		
		assertThat(response).isNotNull();
		assertThat(response.getModel()).isEqualTo("modelx");
	}
	
}
