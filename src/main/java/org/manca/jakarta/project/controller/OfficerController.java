package org.manca.jakarta.project.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import org.manca.jakarta.project.model.util.DataToStart;
import org.manca.jakarta.project.util.service.Officer;

@Path(value = "/officer")
public class OfficerController {
    @Inject
    Officer officer;
    @PUT
    @Path(value = "/start")
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean startRace(DataToStart dataToStart) {
        return officer.startRace(dataToStart.getRaceId(), dataToStart.getCategoryIds());
    }
}