package org.manca.jakarta.project.util;

import java.io.Serializable;
import java.util.List;
public class StartList implements Serializable {
    private List<RawAthlete> rawAthletes;

    private List<RawCategory> rawCategories;

    public StartList() {
    }

    public StartList(List<RawAthlete> rawAthletes, List<RawCategory> rawCategories) {
        setRawAthletes(rawAthletes); setRawCategories(rawCategories);
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

    public List<RawCategory> getRawCategories() {
        return rawCategories;
    }

    public void setRawCategories(List<RawCategory> rawCategories) {
        this.rawCategories = rawCategories;
    }

    public void addRawCategory(RawCategory rawCategory) {
        getRawCategories().add(rawCategory);
    }
}
