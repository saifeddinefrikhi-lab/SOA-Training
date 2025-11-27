package webservices;

import entities.Module;
import entities.UniteEnseignement;
import metiers.ModuleBusiness;
import metiers.UniteEnseignementBusiness;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/modules")
public class ModuleResource {

    private ModuleBusiness moduleBusiness = new ModuleBusiness();
    private UniteEnseignementBusiness ueBusiness = new UniteEnseignementBusiness();

    // 1) Création d'un nouveau Module
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createModule(Module module) {
        try {
            // Vérifier si l'UE existe
            if (module.getUniteEnseignement() != null) {
                UniteEnseignement ue = ueBusiness.getUEByCode(module.getUniteEnseignement().getCode());
                if (ue == null) {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("Unité d'enseignement non trouvée").build();
                }
                module.setUniteEnseignement(ue);
            }

            boolean added = moduleBusiness.addModule(module);
            if (added) {
                return Response.status(Response.Status.CREATED).entity(module).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 2) Récupération de la liste de tous les modules
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllModules() {
        List<Module> modules = moduleBusiness.getAllModules();
        return Response.ok(modules).build();
    }

    // 3) Récupération d'un module par matricule
    @GET
    @Path("/{matricule}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getModuleByMatricule(@PathParam("matricule") String matricule) {
        Module module = moduleBusiness.getModuleByMatricule(matricule);
        if (module != null) {
            return Response.ok(module).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // 4) Suppression d'un module
    @DELETE
    @Path("/{matricule}")
    public Response deleteModule(@PathParam("matricule") String matricule) {
        boolean removed = moduleBusiness.deleteModule(matricule);
        if (removed) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // 5) Modification d'un module
    @PUT
    @Path("/{matricule}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateModule(@PathParam("matricule") String matricule, Module updatedModule) {
        // Vérifier si l'UE existe
        if (updatedModule.getUniteEnseignement() != null) {
            UniteEnseignement ue = ueBusiness.getUEByCode(updatedModule.getUniteEnseignement().getCode());
            if (ue == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Unité d'enseignement non trouvée").build();
            }
            updatedModule.setUniteEnseignement(ue);
        }

        boolean updated = moduleBusiness.updateModule(matricule, updatedModule);
        if (updated) {
            return Response.ok(updatedModule).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // 6) Récupération des modules par code UE
    @GET
    @Path("/UE")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getModulesByUE(@QueryParam("codeUE") int codeUE) {
        UniteEnseignement ue = ueBusiness.getUEByCode(codeUE);
        if (ue == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Unité d'enseignement non trouvée").build();
        }

        List<Module> result = moduleBusiness.getModulesByUE(ue);
        return Response.ok(result).build();
    }

    // Récupération des modules par type
    @GET
    @Path("/type/{type}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getModulesByType(@PathParam("type") String type) {
        try {
            Module.TypeModule typeModule = Module.TypeModule.valueOf(type.toUpperCase());
            List<Module> result = moduleBusiness.getModulesByType(typeModule);
            return Response.ok(result).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Type de module invalide").build();
        }
    }
}