package org.manca.jakarta.project.model;

import jakarta.mail.util.LineInputStream;
import jakarta.persistence.*;

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
	@Column(name = "firstname")
	private String firstname;
	@Column(name = "lastname")
	private String lastname;
	@Column(name = "birth_date")
	private LocalDate birthDate;
	@Column(name = "city")
	private String city;
	@Column(name = "team")
	private String team;
	@ManyToMany(mappedBy = "athletes")
	List<Race> races;
	
	public Athlete() {}

	// GETTER and SETTER
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public List<Race> getRaces() {
		return races;
	}

	public void setRaces(List<Race> races) {
		this.races = races;
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