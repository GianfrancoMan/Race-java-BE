package org.manca.jakarta.project.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
public class Race implements Serializable {
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

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "race_category",
            joinColumns =  @JoinColumn(name = "race_id", referencedColumnName = "id") ,
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"))
    private List<Category> categories= new ArrayList<>();
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "race_athlete",
            joinColumns = @JoinColumn(name = "race_id", referencedColumnName = "id") ,
            inverseJoinColumns = @JoinColumn(name = "athlete_id", referencedColumnName = "id"))
    private List<Athlete> athletes= new ArrayList<>();

    //Constructor

    // Getter and Setter
    public Long getId() {
        return id;
    }


    public String getTitle() {
        return title;
    }


    public String getPlace() {
        return place;
    }
    public String getCity() {
        return city;
    }
    public LocalDateTime getRaceDateTime() {
        return raceDateTime;
    }
    public List<Category> getCategories() {
        return categories;
    }
    public List<Athlete> getAthletes() {
        return athletes;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setPlace(String place) {
        this.place = place;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public void setRaceDateTime(LocalDateTime raceDateTime) {
        this.raceDateTime = raceDateTime;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public void setAthletes(List<Athlete> athletes) {
        this.athletes = athletes;
    }

    //others
    public void addAthlete(Athlete athlete) {
        this.getAthletes().add(athlete);
        athlete.addRace(this);
    }
    public void addCategory(Category category) {
        this.getCategories().add(category);
        category.addRace(this);
    }
}
