package webservices;

import entities.UniteEnseignement;
import metiers.UniteEnseignementBusiness;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/UE")
public class UniteEnseignementResource {

    private UniteEnseignementBusiness ueBusiness = new UniteEnseignementBusiness();

    // 1) Création d'une nouvelle unité d'enseignement
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUE(UniteEnseignement ue) {
        try {
            boolean added = ueBusiness.addUniteEnseignement(ue);
            if (added) {
                return Response.status(Response.Status.CREATED).entity(ue).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 2) Récupération de la liste de toutes les unités d'enseignements
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUEs() {
        List<UniteEnseignement> ueList = ueBusiness.getListeUE();
        return Response.ok(ueList).build();
    }

    // 3) Récupération par semestre
    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUEBySemestre(@QueryParam("semestre") Integer semestre) {
        if (semestre != null) {
            List<UniteEnseignement> result = ueBusiness.getUEBySemestre(semestre);
            return Response.ok(result).build();
        }
        return Response.ok(ueBusiness.getListeUE()).build();
    }

    // 4) Suppression d'une unité d'enseignement
    @DELETE
    @Path("/{code}")
    public Response deleteUE(@PathParam("code") int code) {
        boolean removed = ueBusiness.deleteUniteEnseignement(code);
        if (removed) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // 5) Modification d'une unité d'enseignement
    @PUT
    @Path("/{code}")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUE(@PathParam("code") int code, UniteEnseignement updatedUE) {
        boolean updated = ueBusiness.updateUniteEnseignement(code, updatedUE);
        if (updated) {
            return Response.ok(updatedUE).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // 6) Récupération par code
    @GET
    @Path("/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUEByCode(@PathParam("code") int code) {
        UniteEnseignement ue = ueBusiness.getUEByCode(code);
        if (ue != null) {
            return Response.ok(ue).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // Récupération par domaine
    @GET
    @Path("/domaine/{domaine}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUEByDomaine(@PathParam("domaine") String domaine) {
        List<UniteEnseignement> result = ueBusiness.getUEByDomaine(domaine);
        return Response.ok(result).build();
    }
}