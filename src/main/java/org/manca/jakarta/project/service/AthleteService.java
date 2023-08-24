package org.manca.jakarta.project.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.manca.jakarta.project.dao.AthleteDao;
import org.manca.jakarta.project.model.Athlete;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Stateless
public class AthleteService {

    @Inject
    private AthleteDao ad;

    public List<Athlete>  findAllAthletes() {
        return ad.findAll();
    }

    public boolean saveAthlete(Athlete athlete) {
        this.makeLowerCase(athlete);
        Athlete persistAthlete =  ad.save(athlete);
        return persistAthlete != null;
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

    /**
     * find all athletes that hav the birthdate equal to the 'birthdate' passed as parameter in the form of a string
     * return a List of Athlete objects if success else null.
     */
    public List<Athlete> findAthletesByBirthDate(String birthdate) {
        LocalDate birthDate = null;
        if(birthdate != null) {
            try {
                birthDate = LocalDate.parse(birthdate);
                return ad.athletesByDate(birthDate);
            } catch (DateTimeParseException e) {
                System.out.println("CUSTOM ERROR: org.manca.jakarta.project.service.AthleteService.findAthletesByBirthDate[Date Parsing Failed]");
                return null;
            }
        }
        return null;
    }

    /**
     * Implements pagination to get all athletes of the race that have the string 'subName' passed as parameter
     * ito their complete name.
     * Pagination leverage on 'pageNumber' and 'pageSize' passed as parameter
     * Returns a List of Athletes objects if success else null.
     */
    public List<Athlete> findAthletesBySubName(String subName, int pageNumber, int pageSize) {
        if(subName != null) {
            return ad.athletesBySubName(subName, pageNumber, pageSize);
        }
        return null;
    }

    /*To make lowercase the string attribute in 'Athlete' instance*/
    private void makeLowerCase(Athlete athlete) {
        athlete.setTeam(athlete.getTeam().toLowerCase());
        athlete.setCity(athlete.getCity().toLowerCase());
        athlete.setFirstname(athlete.getFirstname().toLowerCase());
        athlete.setLastname(athlete.getLastname().toLowerCase());
    }

}
