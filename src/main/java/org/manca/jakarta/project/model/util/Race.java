package org.manca.jakarta.project.model.util;

import java.time.LocalDateTime;

/*
    lA  CLASSE  'Race' SPECIFICA I DATI BASE DELL'EVENTO GARA 'title' INDICA IL TITOLO CHE VIENE ASSEGNATO ALL'EVENTO GARA
    'place' INDICA IL LUOGO DEL RITROVO, 'city' LA CITTA'  'raceDateTime' INDICA DATA E ORA DEL RITROVO, 'organizar' INDICA
    L'ASSOCIAZIONE O L'ENTE ORGANIZZATORE DELL'EVENTO.
 */
public class Race {
    private String title;
    private String place;
    private String city;
    private LocalDateTime raceDateTime;

    public Race() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDateTime getRaceDateTime() {
        return raceDateTime;
    }

    public void setRaceDateTime(LocalDateTime raceDateTime) {
        this.raceDateTime = raceDateTime;
    }
}
