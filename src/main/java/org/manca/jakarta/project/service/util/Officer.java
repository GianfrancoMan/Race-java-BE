package org.manca.jakarta.project.service.util;

import org.manca.jakarta.project.model.util.Category;
import org.manca.jakarta.project.model.util.Race;
import org.manca.jakarta.project.model.util.RawAthlete;

import java.util.List;

/*This class models the behavior of a race officer*/
public class Officer {
    private Race race;
    private Category category;
    private List<RawAthlete> rawAthletes;

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<RawAthlete> getAllRawAthletes() {
        return rawAthletes;
    }

    public String addRawAthlete(RawAthlete rawAthlete) {
        String message = "successful";
        if(rawAthlete != null && this.checkRaceNumber(rawAthlete.getRaceNumber())) {
            this.rawAthletes.add(rawAthlete);

            return message;
        }
        message = "failed: race number must be unique";
        return message;
    }

    public RawAthlete getRowAthlete(String raceNumber) {

       for(RawAthlete ra : rawAthletes) {
           if (ra.getRaceNumber().equalsIgnoreCase(raceNumber))
               return ra;
       }
       return null;
    }

    /*Verifies if the race is already present in the list of raw athletes*/
    private boolean checkRaceNumber(String raceNumber) {
        for (RawAthlete rn : getAllRawAthletes()) {
            if(rn.getRaceNumber().equalsIgnoreCase(raceNumber))
                return false;
        }
        return true;
    }
}
