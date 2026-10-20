
package filtres;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext)
            throws IOException {

        // Récupérer l'en-tête Authorization
        String authorizationHeader =
                requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Vérifier la présence du token Bearer
        if (authorizationHeader == null
                || !authorizationHeader.startsWith("Bearer ")) {

            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .entity("Token JWT manquant ou invalide")
                            .build()
            );
            return;
        }

        // Extraire le token
        String token = authorizationHeader.substring("Bearer ".length());

        try {
            // Vérifier la signature et l'expiration du token
            Claims claims = JwtUtil.validateToken(token);

            // Vérifier que le token possède un sujet
            if (claims.getSubject() == null
                    || claims.getSubject().isBlank()) {

                requestContext.abortWith(
                        Response.status(Response.Status.UNAUTHORIZED)
                                .entity("Token JWT invalide")
                                .build()
                );
            }

        } catch (JwtException | IllegalArgumentException e) {

            // Token expiré, mal signé ou mal formé
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .entity("Token JWT invalide ou expiré")
                            .build()
            );
        }
    }
}
