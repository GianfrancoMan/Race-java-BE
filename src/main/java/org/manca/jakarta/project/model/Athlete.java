package org.manca.jakarta.project.model;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.mail.util.LineInputStream;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/*
La classe 'Athlete' finge da Entity Model per la tabella 'Athletes' del database 'race'.
I dati completi degli atleti verranno memorizzati nel database, inoltre questa classe è
in relazione diretta con la classe 'RawAthlete' che verra utilizzato dalla classe 'Officer'
per gestire lo sviluppo della gara.
 */
@Entity
@Table(name = "athletes")
@Cacheable(false)
public class Athlete {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "firstname", nullable = false)
	private String firstname;
	@Column(name = "lastname", nullable = false)
	private String lastname;
	@Column(name = "birth_date", nullable = false)
	private LocalDate birthDate;
	@Column(name = "city")
	private String city;
	@Column(name = "team")
	private String team;
	@JsonbTransient
	@ManyToMany(mappedBy = "athletes")
	List<Race> races;

	// GETTER and SETTER
	public Long getId() {
		return id;
	}
	public String getFirstname() {
		return firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public LocalDate getBirthDate() {
		return birthDate;
	}
	public String getCity() {
		return city;
	}
	public String getTeam() {
		return team;
	}
	public List<Race> getRaces() {
		return races;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public void setTeam(String team) {
		this.team = team;
	}

	public void setRaces(List<Race> races) {
		this.races = races;
	}

	public void addRace(Race race) {
		getRaces().add(race);
	}
	@Override
	public String toString() {
		return "Athlete{" +
				"id=" + id +
				", firstname='" + firstname + '\'' +
				", lastname='" + lastname + '\'' +
				", birthDate=" + birthDate +
				", city='" + city + '\'' +
				", team='" + team + '\'' +
				'}';
	}
}