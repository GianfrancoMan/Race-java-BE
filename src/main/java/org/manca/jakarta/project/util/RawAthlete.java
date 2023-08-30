package org.manca.jakarta.project.util;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/*
    LA CLASSE  'RawAthlete'  E' IN RELAZIONE CON LA CLASSE 'Athlete'  TRAMITE IL SUO ATTRIBUTO 'id' , MENTRE
    L'ATTRIBUTO  'raceNumber' E' IL NUMERO DI GARA CHE VERRA' ASSOCIATO ALL'ATLETA IN GARA  IN FASE DI ISCRI-
    ZIONE L'ATTRIBUTO 'category' INDICA LA CATEGORIA DI APPARTENENZA DELL'ATLTETA IN GARA.
 */
public class RawAthlete implements Serializable {
    // Declarations
    private long idAthlete;
    private String raceNumber;
    private long idCategory;
    private int lapsToRun;
    private List<LocalTime> timeOnLaps;
    private boolean state;

    // Constructor
    public RawAthlete(long idAthlete, String raceNumber, long idCategory, int lapsToRun) {
        setIdAthlete(idAthlete);
        setRaceNumber(raceNumber);
        setIdCategory(idCategory);
        setLapsToRun(lapsToRun);
        this.timeOnLaps= new ArrayList<>(getLapsToRun());
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

    public long getIdCategory() {
        return idCategory;
    }

    public int getLapsToRun() {
        return lapsToRun;
    }

    public void setLapsToRun(int lapsToRun) {
        this.lapsToRun = lapsToRun;
    }

    public void setIdCategory(long idCategory) {
        this.idCategory = idCategory;
    }

    public List<LocalTime> getTimeOnLaps() {
        return timeOnLaps;
    }

    public void setTimeOnLaps(List<LocalTime> timeOnLaps) {
        this.timeOnLaps = timeOnLaps;
    }

    public void addTimeOnLaps(LocalTime timeOnLaps) {
        this.timeOnLaps.add(timeOnLaps);
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
