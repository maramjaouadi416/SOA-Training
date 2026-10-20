
package ressources;

import filtres.JwtUtil;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path("/authentication")
public class AuthenticationEndPoint {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticate(Map<String, String> credentials) {

        if (credentials == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Identifiants manquants"))
                    .build();
        }

        String username = credentials.get("username");
        String password = credentials.get("password");

        // Identifiants de démonstration pour tester l'atelier.
        if ("etudiant".equals(username) && "Etudiant123!".equals(password)) {

            String token = JwtUtil.generateToken(username);

            return Response.ok(Map.of("token", token)).build();
        }

        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(Map.of("error", "Identifiants incorrects"))
                .build();
    }
}
