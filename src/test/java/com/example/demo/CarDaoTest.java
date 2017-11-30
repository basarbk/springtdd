package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class CarDaoTest {
	
	@Autowired
	TestEntityManager testEntityManager;
	
	@Autowired
	CarDao carDao;
	
	@Test
	public void shouldReturnEmptyListByBrand(){
		List<Car> cars = carDao.findByBrand("tesla");
		assertThat(cars.size()).isEqualTo(0);
	}
	
	@Test
	public void shouldReturnListofcarsByBrand(){
		testEntityManager.persistAndFlush(new Car(0, "tesla", "model s", 2015));
		List<Car> cars = carDao.findByBrand("tesla");
		assertThat(cars.size()).isEqualTo(1);
		assertThat(cars.get(0).getBrand()).isEqualToIgnoringCase("tesla");
	}
	

	@Test
	public void shouldReturnListofcarsByModel(){
		testEntityManager.persistAndFlush(new Car(0, "tesla", "model s", 2015));
		List<Car> cars = carDao.findByModel("model s");
		assertThat(cars.size()).isEqualTo(1);
		assertThat(cars.get(0).getBrand()).isEqualToIgnoringCase("tesla");
	}

}
