package com.example.demo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"brand", "model"}))
public class Car {
	
	@Id @GeneratedValue
	private long id;
	
	@NotNull
	@Size(min=3, max=20)
	private String brand;
	
	@NotNull
	@Size(min=3, max=20)
	private String model;
	
	private int year; 

}
