package org.manca.jakarta.project.util;

import org.manca.jakarta.project.model.Category;
import org.manca.jakarta.project.model.Race;

import java.util.List;

/*This class models the behavior of a race officer*/
public class Officer {
    // Declarations
    private Race race;
    private List<Category> categories;
    private List<RawAthlete> rawAthletes;

    // GETTER and SETTER
    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public List<Category> getAllCategories() {
        return categories;
    }

    public boolean addCategory(Category category) {
        if(category != null && this.checkCategory(category.getTitle())) {
            this.categories.add(category);
            return true;
        }
        return false;
    }

    public Category getCategory(String title) {
        return categories.stream().filter(c-> c.getTitle().equalsIgnoreCase(title)).findFirst().orElse(null);
    }

    public List<RawAthlete> getAllRawAthletes() {
        return rawAthletes;
    }

    public boolean addRawAthlete(RawAthlete rawAthlete) {
        if(rawAthlete != null && this.checkRaceNumber(rawAthlete.getRaceNumber())) {
            this.rawAthletes.add(rawAthlete);
            return true;
        }
        return false;
    }

    public RawAthlete getRowAthlete(String raceNumber) {

       for(RawAthlete ra : rawAthletes) {
           if (ra.getRaceNumber().equalsIgnoreCase(raceNumber))
               return ra;
       }
       return null;
    }

    /*Verifies if the race is already present in the list of raw athletes*/
    private boolean checkCategory(String raceNumber) {
        for (RawAthlete rn : getAllRawAthletes()) {
            if(rn.getRaceNumber().equalsIgnoreCase(raceNumber))
                return false;
        }
        return true;
    }
    /*Verifies if the category is already present in the list of the categories*/
    private boolean checkRaceNumber(String title) {
        for (Category c : getAllCategories()) {
            if(c.getTitle().equalsIgnoreCase(title))
                return false;
        }
        return true;
    }
}
