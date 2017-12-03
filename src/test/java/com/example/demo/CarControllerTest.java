package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CarControllerTest {
	
	@Autowired
	TestRestTemplate testRestTemplate;
	
	@Autowired
	CarDao carDao;
	
	@Test
	public void shouldReturnEmptyList(){
		ResponseEntity<List<Car>> response= testRestTemplate.exchange("/api/cars", HttpMethod.GET, null, new ParameterizedTypeReference<List<Car>>() {});
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(response.getBody().size()).isEqualTo(0);
	}
	
	@Test
	public void shouldReturnListWithData(){
		carDao.save(new Car(0L, "tesla","model s",121312));
		ResponseEntity<List<Car>> response= testRestTemplate.exchange("/api/cars", HttpMethod.GET, null, new ParameterizedTypeReference<List<Car>>() {});
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(response.getBody().size()).isEqualTo(1);
	}

	@Test
	public void shouldReturn404ForUnknownId(){
		ResponseEntity<Error> response= testRestTemplate.getForEntity("/api/cars/1", Error.class);
		assertThat(response.getStatusCodeValue()).isEqualTo(404);
		assertThat(response.getBody().getMessage()).isEqualTo("Car not found");
	}
	
	@Test
	public void shouldReturnCarWithKnownId(){
		Car car = carDao.save(new Car(0L, "tesla","model s",121312));
		ResponseEntity<Car> response= testRestTemplate.getForEntity("/api/cars/"+car.getId(), Car.class);
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(response.getBody().getBrand()).isEqualTo("tesla");
	}
	
	@Test
	public void shouldCarPostFailWithValidation(){
		Car car = new Car(0L, "", "", 111);
		ResponseEntity<ValidationError> response = testRestTemplate.postForEntity("/api/cars", car, ValidationError.class);
		assertThat(response.getStatusCodeValue()).isEqualTo(400);
		assertThat(response.getBody().getErrors().containsKey("brand")).isTrue();
		assertThat(response.getBody().getErrors().containsKey("model")).isTrue();
	}
	
	@Test
	public void shouldCarPostFailWithConstraint(){
		carDao.save(new Car(0L, "tesla","model s",121312));
		Car newCar = new Car(0L, "tesla", "model s", 111);
		ResponseEntity<Error> response = testRestTemplate.postForEntity("/api/cars", newCar, Error.class);
		assertThat(response.getStatusCodeValue()).isEqualTo(409);
		assertThat(response.getBody().getPath()).isEqualTo("/api/cars");
		assertThat(response.getBody().getError()).isEqualTo("Conflict");
	}
	
	@Test
	public void shouldCarPostSuccess(){
		Car newCar = new Car(0L, "tesla", "model s", 111);
		ResponseEntity<Car> response = testRestTemplate.postForEntity("/api/cars", newCar, Car.class);
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(response.getBody().getId()).isGreaterThan(0L);
	}
	
	@Test
	public void shouldCarUpdateFail404() throws JsonProcessingException{
		Car newCar = new Car(0L, "tesla", "model s", 111);
		ResponseEntity<Error> response = testRestTemplate.exchange("/api/cars/1", HttpMethod.PUT, getEntity(newCar), Error.class);
		assertThat(response.getStatusCodeValue()).isEqualTo(404);
		assertThat(response.getBody().getMessage()).isEqualToIgnoringCase("Car not found");
	}

	private HttpEntity<String> getEntity(Car newCar) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();		
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(mapper.writeValueAsString(newCar), header);
		return entity;
	}
	
	@After
	public void after(){
		carDao.deleteAll();
	}
	
	
}
