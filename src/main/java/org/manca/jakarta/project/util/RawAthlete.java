package org.manca.jakarta.project.util;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * RawAthlete instances represent the state of the athlete that performing a race,
 * it will be make persistent by serialization and stored in the user machine as part of
 * StartList serializable object that represents the start list of the race
 */
public class RawAthlete implements Serializable {
    // Declarations

    /** idAthlete is related to the id of Athlete Entity */
    private long idAthlete;

    /** raceNumber is the unique race number of the athlete */
    private String raceNumber;

    /** idCategory is related to the id of Category Entity */
    private long idCategory;

    /** timeOnLaps is the list of the athlete's lap times */
    private List<LocalTime> timeOnLaps;

    /** state indicates if the athlete has started or not*/
    private boolean state = false;

    // Constructor

    public RawAthlete() {
    }

    public RawAthlete(long idAthlete, String raceNumber, long idCategory) {
        setIdAthlete(idAthlete);
        setRaceNumber(raceNumber);
        setIdCategory(idCategory);
        this.timeOnLaps= new ArrayList<>();
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

    @Override
    public String toString() {
        return "RawAthlete{" +
                "idAthlete=" + idAthlete +
                ", raceNumber='" + raceNumber + '\'' +
                ", idCategory=" + idCategory +
                ", timeOnLaps=" + timeOnLaps +
                ", state=" + state +
                '}';
    }
}
