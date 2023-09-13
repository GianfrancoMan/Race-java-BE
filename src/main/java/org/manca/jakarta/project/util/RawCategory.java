package org.manca.jakarta.project.util;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * RawCategory instances represent the state of a category during a specific race,
 * it will be make persistent by serialization and stored in the user machine as part of
 * StartList serializable object that represents the start list of the race
 */
public class RawCategory implements Serializable {

    /** idCategory is related to the id of Category Entity */
    private long idCategory;

    /** The number of laps that each athlete belonging to this category should perform */
    private int lapsNumber;

    /** The time at which the athlete belonging to this category start the race */
    private LocalTime raceStartTime = LocalTime.of(0,0,0);

    public RawCategory() {
    }

    public RawCategory(Long idCategory) {
        this.idCategory = idCategory;
    }

    public Long getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(Long idCategory) {
        this.idCategory = idCategory;
    }

    public int getLapsNumber() {
        return lapsNumber;
    }

    public void setLapsNumber(int lapsNumber) {
        this.lapsNumber = lapsNumber;
    }

    public LocalTime getRaceStartTime() {
        return raceStartTime;
    }

    public void setRaceStartTime(LocalTime raceStartTime) {
        this.raceStartTime = raceStartTime;
    }

    @Override
    public String toString() {
        return "RawCategory{" +
                "idCategory=" + idCategory +
                ", lapsNumber=" + lapsNumber +
                ", raceStartTime=" + raceStartTime +
                '}';
    }
}
