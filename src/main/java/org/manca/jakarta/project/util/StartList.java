package org.manca.jakarta.project.util;

import java.io.Serializable;
import java.util.List;
public class StartList implements Serializable {
    private List<RawAthlete> rawAthletes;

    public StartList(List<RawAthlete> rawAthletes) {
        setRawAthletes(rawAthletes);
    }

    public List<RawAthlete> getRawAthletes() {
        return rawAthletes;
    }

    public void setRawAthletes(List<RawAthlete> rawAthletes) {
        this.rawAthletes = rawAthletes;
    }

    public void addRawAthlete(RawAthlete rawAthlete) {
        rawAthletes.add(rawAthlete);
    }
}
