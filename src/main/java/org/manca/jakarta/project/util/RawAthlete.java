package org.manca.jakarta.project.util;

import org.manca.jakarta.project.model.Category;

import java.time.LocalTime;

/*
    LA CLASSE  'RawAthlete'  E' IN RELAZIONE CON LA CLASSE 'Athlete'  TRAMITE IL SUO ATTRIBUTO 'id' , MENTRE
    L'ATTRIBUTO  'raceNumber' E' IL NUMERO DI GARA CHE VERRA' ASSOCIATO ALL'ATLETA IN GARA  IN FASE DI ISCRI-
    ZIONE L'ATTRIBUTO 'category' INDICA LA CATEGORIA DI APPARTENENZA DELL'ATLTETA IN GARA.
 */
public class RawAthlete {
    // Declarations
    private long idAthlete;

    private String raceNumber;
    private Category category;
    private LocalTime[] timeOnLaps;
    private boolean state;

    // Constructor
    public RawAthlete() {
        setState(false);
    }

    //GETTER AND SETTER

    public long getIdAthlete() {
        return idAthlete;
    }

    public void setIdAthlete(long id) {
        this.idAthlete = id;
    }

    public String getRaceNumber() {
        return raceNumber;
    }

    public void setRaceNumber(String raceNumber) {
        this.raceNumber = raceNumber;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
        timeOnLaps= new LocalTime[category.getLapsNumber()];
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
