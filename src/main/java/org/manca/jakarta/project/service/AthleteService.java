package org.manca.jakarta.project.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.manca.jakarta.project.dao.AthleteDao;
import org.manca.jakarta.project.model.Athlete;

import java.util.List;

@Stateless
public class AthleteService {
    //this static attribute is going to contain the last id created in the athletes table
    // the idea is to use this value each time I need to create RawAthlete instance
    // because it needs to know the athlete's id that it is bound
    private static long lastPersistId = 0;

    @Inject
    private AthleteDao ad;

    public List<Athlete>  findAllAthletes() {
        return ad.findAll();
    }

    public boolean saveAthlete(Athlete athlete) {
        this.makeLowerCase(athlete);
        Athlete pesistAthlete =  ad.save(athlete);
        if(pesistAthlete!=null) {
            AthleteService.lastPersistId = pesistAthlete.getId();
            return true;
        }
        return false;
    }

    public Athlete findAthlete(long id) {
        return  ad.findById(id);
    }

    public boolean updateAthlete(Athlete athlete) {
        this.makeLowerCase(athlete);
        return ad.update(athlete);
    }

    public boolean deleteAthlete(long id) {
        return ad.delete(id);
    }

    /*To make lowercase the string attribute in 'Athlete' instance*/
    private void makeLowerCase(Athlete athlete) {
        athlete.setTeam(athlete.getTeam().toLowerCase());
        athlete.setCity(athlete.getCity().toLowerCase());
        athlete.setFirstname(athlete.getFirstname().toLowerCase());
        athlete.setLastname(athlete.getLastname().toLowerCase());
    }

    public static long getLastPersistId() {
        return lastPersistId;
    }
}
