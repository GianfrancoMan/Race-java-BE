package org.manca.jakarta.project.model.util;

import java.time.LocalTime;
/*
    LA CLASSE 'Category' MODELLA LA CATEGORIA DI GARA ASSOCIATA AD UN ATLETA L'ATTRIBUTO 'title' INDICA IL NOME ASSEGNATO
    ALLA CATEGORIA, LATTRIBUTO 'lapsNumber' INDICA IL NUMERO DI GIRI CHEL'ATLETA ASSOCIATO ALA CATEGORIA DOVRA' PERCOR-
    RERE SUL CIRQUITO, L'ATTRIBUTO 'raceStartTime' INDICA IL MOMENTO DELLA PARTENZA PER GLI ATLETI ASSOCIATI ALLA COTEGO-
    RIA E SARA' FONDAMENTALE PER STILARELA CLASSIFICA.
 */
public class Category {
    private String title;
    private short lapsNumber;
    private LocalTime raceStartTime;

    public Category() {
    }

    public Category(String title, short lapsNumber, LocalTime raceStartTime) {
        this.title = title;
        this.lapsNumber = lapsNumber;
        this.raceStartTime = raceStartTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public short getLapsNumber() {
        return lapsNumber;
    }

    public void setLapsNumber(short lapsNumber) {
        this.lapsNumber = lapsNumber;
    }

    public LocalTime getRaceStartTime() {
        return raceStartTime;
    }

    public void setRaceStartTime(LocalTime raceStartTime) {
        this.raceStartTime = raceStartTime;
    }
}
