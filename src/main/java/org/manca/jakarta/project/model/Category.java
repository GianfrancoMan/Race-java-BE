package org.manca.jakarta.project.model;

import jakarta.json.bind.annotation.JsonbTransient;
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
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "laps")
    private short lapsNumber;
    @Column(name = "start_time")
    private LocalTime raceStartTime;
    @JsonbTransient
    @ManyToMany(mappedBy = "categories")
    List<Race> races;

    // GETTER and SETTER
    public Long getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public short getLapsNumber() {
        return lapsNumber;
    }
    public LocalTime getRaceStartTime() {
        return raceStartTime;
    }
    public List<Race> getRaces() {
        return races;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setLapsNumber(short lapsNumber) {
        this.lapsNumber = lapsNumber;
    }
    public void setRaceStartTime(LocalTime raceStartTime) {
        this.raceStartTime = raceStartTime;
    }
    public void setRaces(List<Race> races) {
        this.races = races;
    }
    public void addRace(Race race) {
        getRaces().add(race);
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
