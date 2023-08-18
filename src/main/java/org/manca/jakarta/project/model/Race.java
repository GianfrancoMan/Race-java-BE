package org.manca.jakarta.project.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

/*
    lA  CLASSE  'Race' SPECIFICA I DATI BASE DELL'EVENTO GARA 'title' INDICA IL TITOLO CHE VIENE ASSEGNATO ALL'EVENTO GARA
    'place' INDICA IL LUOGO DEL RITROVO, 'city' LA CITTA'  'raceDateTime' INDICA DATA E ORA DEL RITROVO, 'organizar' INDICA
    L'ASSOCIAZIONE O L'ENTE ORGANIZZATORE DELL'EVENTO, INOLTRE E' IN RELAZIONE ManyTOMany CON LE CLASSI
    Category e Athelte
 */
@Entity
@Table(name="races")
@Cacheable(false)
public class Race {
    //declaration
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "place")
    private String place;
    @Column(name = "city")
    private String city;
    @Column(name = "date_time")
    private LocalDateTime raceDateTime;
    @ManyToMany
    private List<Category> raceCategories;
    @ManyToMany
    private List<Athlete> athletes;

    //Constructor
    public Race() {
    }

    // Getter and Setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDateTime getRaceDateTime() {
        return raceDateTime;
    }

    public void setRaceDateTime(LocalDateTime raceDateTime) {
        this.raceDateTime = raceDateTime;
    }

    public List<Category> getCategories() {
        return raceCategories;
    }

    public void setCategories(List<Category> categories) {
        this.raceCategories = categories;
    }

    public List<Athlete> getAthletes() {
        return athletes;
    }

    public void setAthletes(List<Athlete> athletes) {
        this.athletes = athletes;
    }

    //others
    public void addAthlete(Athlete athlete) {
        this.getAthletes().add(athlete);
    }

    public void addCategory(Category category) {
        this.getCategories().add(category);
    }
}
