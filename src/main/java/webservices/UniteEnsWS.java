package webservices;

import entities.UniteEnseignement;
import metiers.UniteEnseignementBusiness;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/UE")
public class UniteEnsWS {

    // Instance partagée de la logique métier
    private static UniteEnseignementBusiness business = new UniteEnseignementBusiness();

    // 1. Récupérer toutes les UE ou filtrer par semestre/code (ex: /rest/UE ou /rest/UE?semestre=2 ou /rest/UE?code=123)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUnitesEnseignement(@QueryParam("semestre") Integer semestre, @QueryParam("code") Integer code) {
        if (semestre != null) {
            List<UniteEnseignement> list = business.getUEBySemestre(semestre);
            return Response.status(200).entity(list).build();
        }
        if (code != null) {
            UniteEnseignement ue = business.getUEByCode(code);
            if (ue != null) {
                return Response.status(200).entity(ue).build();
            } else {
                return Response.status(404).entity("Unité d'enseignement non trouvée").build();
            }
        }
        List<UniteEnseignement> all = business.getListeUE();
        return Response.status(200).entity(all).build();
    }

    // 2. Créer une nouvelle UE (Requête POST /rest/UE)
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUE(UniteEnseignement ue) {
        boolean created = business.addUniteEnseignement(ue);
        if (created) {
            return Response.status(201).entity(ue).build();
        }
        return Response.status(404).build();
    }

    // 3. Modifier une UE par son code (Requête PUT /rest/UE/101)
    @PUT
    @Path("/{code}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUE(@PathParam("code") int code, UniteEnseignement updatedUE) {
        boolean updated = business.updateUniteEnseignement(code, updatedUE);
        if (updated) {
            return Response.status(200).entity(updatedUE).build();
        }
        return Response.status(404).entity("Unité d'enseignement introuvable pour modification").build();
    }

    // 4. Supprimer une UE par son code (Requête DELETE /rest/UE/102)[cite: 1]
    @DELETE
    @Path("/{code}")
    public Response deleteUE(@PathParam("code") int code) {
        boolean deleted = business.deleteUniteEnseignement(code);
        if (deleted) {
            return Response.status(200).entity("Suppression réussie").build();
        }
        return Response.status(404).entity("Unité d'enseignement introuvable pour suppression").build();
    }
}