package com.thecorporateer.influence.objects;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.thecorporateer.influence.controllers.Views;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

@Entity
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Division extends JpaEntity {
	
	public Division(String name, Department department) {
		this.name = name;
		this.department = department;
	}

	@NotNull
	@NotBlank
	@JsonView(Views.Corporateer.class)
	private String name;
	@NotNull
	@ManyToOne
	@JsonView(Views.Corporateer.class)
	private Department department;
	@OneToMany(mappedBy = "mainDivision")
	private List<Corporateer> corporateers;
	@OneToMany(mappedBy = "division")
	private List<Influence> influence;

}
