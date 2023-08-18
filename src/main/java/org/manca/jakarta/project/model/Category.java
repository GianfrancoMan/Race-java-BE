package org.manca.jakarta.project.model;

import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.List;

/*
    LA CLASSE 'Category' MODELLA LA CATEGORIA DI GARA ASSOCIATA AD UN ATLETA L'ATTRIBUTO 'title' INDICA IL NOME ASSEGNATO
    ALLA CATEGORIA, LATTRIBUTO 'lapsNumber' INDICA IL NUMERO DI GIRI CHEL'ATLETA ASSOCIATO ALA CATEGORIA DOVRA' PERCOR-
    RERE SUL CIRQUITO, L'ATTRIBUTO 'raceStartTime' INDICA IL MOMENTO DELLA PARTENZA PER GLI ATLETI ASSOCIATI ALLA COTEGO-
    RIA E SARA' FONDAMENTALE PER STILARELA CLASSIFICA.
 */
@Entity
@Table(name = "categories")
@Cacheable(value = false)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "laps")
    private short lapsNumber;
    @Column(name = "start_time")
    private LocalTime raceStartTime;
    @ManyToMany(mappedBy = "raceCategories")
    List<Race> races;



    public Category() { }

    public Category(String title, short lapsNumber, LocalTime raceStartTime) {
        this.title = title;
        this.lapsNumber = lapsNumber;
        this.raceStartTime = raceStartTime;
    }

    // GETTER and SETTER
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

    public short getLapsNumber() {
        return lapsNumber;
    }

    public void setLapsNumber(short lapsNumber) {
        this.lapsNumber = lapsNumber;
    }

    public LocalTime getRaceStartTime() {
        return raceStartTime;
    }

    public void setRaceStartTime(LocalTime raceStartTime) {
        this.raceStartTime = raceStartTime;
    }

    public List<Race> getRaces() {
        return races;
    }

    public void setRaces(List<Race> races) {
        this.races = races;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", lapsNumber=" + lapsNumber +
                ", raceStartTime=" + raceStartTime +
                ", races=" + races +
                '}';
    }
}
