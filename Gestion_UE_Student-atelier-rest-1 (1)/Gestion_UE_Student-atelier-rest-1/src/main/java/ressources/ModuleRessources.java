package ressources;

import entities.Module;
import entities.UniteEnseignement;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import metiers.ModuleBusiness;
import metiers.UniteEnseignementBusiness;

import java.util.List;

@Path("/modules")
public class ModuleRessources {

    private static final ModuleBusiness moduleBusiness = new ModuleBusiness();
    private static final UniteEnseignementBusiness ueBusiness =
            new UniteEnseignementBusiness();

    // 1. Récupérer tous les modules
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Module> getAllModules() {
        return moduleBusiness.getAllModules();
    }

    // 2. Récupérer un module par sa matricule
    @GET
    @Path("/{matricule}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getModuleByMatricule(
            @PathParam("matricule") String matricule) {

        Module module = moduleBusiness.getModuleByMatricule(matricule);

        if (module == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(module).build();
    }

    // 3. Ajouter un module
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addModule(Module module) {

        if (module == null || module.getUniteEnseignement() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        boolean added = moduleBusiness.addModule(module);

        if (!added) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("UE introuvable ou module invalide")
                    .build();
        }

        return Response.status(Response.Status.CREATED)
                .entity(module)
                .build();
    }

    // 4. Supprimer un module
    @DELETE
    @Path("/{matricule}")
    public Response deleteModule(
            @PathParam("matricule") String matricule) {

        boolean deleted = moduleBusiness.deleteModule(matricule);

        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.noContent().build();
    }

    // 5. Modifier un module
    @PUT
    @Path("/{matricule}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateModule(
            @PathParam("matricule") String matricule,
            Module module) {

        if (module == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if (module.getUniteEnseignement() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        UniteEnseignement ue = ueBusiness.getUEByCode(
                module.getUniteEnseignement().getCode());

        if (ue == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("UE introuvable")
                    .build();
        }

        Module existing = moduleBusiness.getModuleByMatricule(matricule);

        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        module.setMatricule(matricule);
        module.setUniteEnseignement(ue);

        boolean updated = moduleBusiness.updateModule(matricule, module);

        if (!updated) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(module).build();
    }

    // 6. Récupérer les modules associés à une UE
    @GET
    @Path("/UE")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getModulesByUE(@QueryParam("codeUE") int codeUE) {

        UniteEnseignement ue = ueBusiness.getUEByCode(codeUE);

        if (ue == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<Module> modules = moduleBusiness.getModulesByUE(ue);

        return Response.ok(modules).build();
    }
}