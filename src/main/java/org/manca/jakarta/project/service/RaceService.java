package org.manca.jakarta.project.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jdk.dynalink.linker.LinkerServices;
import org.manca.jakarta.project.dao.RaceDao;
import org.manca.jakarta.project.model.Athlete;
import org.manca.jakarta.project.model.Category;
import org.manca.jakarta.project.model.Race;

import java.util.List;

@Stateless
public class RaceService {
    @Inject
    private RaceDao rd;

    public Race savaRace(Race race) {
        this.makeLowerCase(race);
        return rd.save(race);
    }

    public List<Race> findAllRaces() {
        return rd.findAll();
    }

    public Race findRaceById(Long id) {
        return rd.findById(id);
    }

    public boolean addAthlete(Long raceId, Long athleteId) {
        if (raceId!=null && athleteId!=null && raceId>=1 && athleteId>=1) {
            return rd.addAthlete(raceId, athleteId);
        }
        return false;
    }

    public boolean addCategory(Long raceId, Long categoryId) {
        if (raceId!=null && categoryId!=null && raceId>=1 && categoryId>=1) {
            return rd.addCategory(raceId, categoryId);
        }
        return false;
    }

    public Race updateRace(Race race) {
        this.makeLowerCase(race);
        return rd.update(race);
    }

    private void makeLowerCase(Race race) {
        race.setCity(race.getCity().toLowerCase());
        race.setPlace(race.getPlace().toLowerCase());
        race.setTitle(race.getTitle().toLowerCase());
    }
}
