package ressources;

import entities.UniteEnseignement;
import metiers.UniteEnseignementBusiness;
import filtres.Secured;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/UE")
public class GestionUeRessources {

    private static final UniteEnseignementBusiness business =
            new UniteEnseignementBusiness();

    // 1. Ajouter une UE
    @Secured
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public Response ajouterUE(UniteEnseignement ue) {

        boolean resultat =
                business.addUniteEnseignement(ue);

        if (resultat) {
            return Response.status(
                    Response.Status.CREATED
            ).build();
        }

        return Response.status(
                Response.Status.NOT_FOUND
        ).build();
    }

    // 2. Afficher toutes les UE
    // 3. Filtrer par semestre
    // 6. Rechercher par code

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUE(
            @QueryParam("semestre") Integer semestre,
            @QueryParam("code") Integer code) {

        if (code != null) {

            UniteEnseignement ue =
                    business.getUEByCode(code);

            if (ue == null) {
                return Response.status(
                        Response.Status.NOT_FOUND
                ).build();
            }

            return Response.ok(ue).build();
        }

        if (semestre != null) {

            return Response.ok(
                    business.getUEBySemestre(semestre)
            ).build();
        }

        return Response.ok(
                business.getListeUE()
        ).build();
    }

    // 4. Supprimer une UE

    @DELETE
    @Path("/{code}")
    public Response supprimerUE(
            @PathParam("code") int code) {

        boolean resultat =
                business.deleteUniteEnseignement(code);

        if (resultat) {
            return Response.ok().build();
        }

        return Response.status(
                Response.Status.NOT_FOUND
        ).build();
    }

    // 5. Modifier une UE

    @PUT
    @Path("/{code}")
    @Consumes(MediaType.APPLICATION_XML)
    public Response modifierUE(
            @PathParam("code") int code,
            UniteEnseignement ue) {

        boolean resultat =
                business.updateUniteEnseignement(
                        code, ue
                );

        if (resultat) {
            return Response.ok().build();
        }

        return Response.status(
                Response.Status.NOT_FOUND
        ).build();
    }
}