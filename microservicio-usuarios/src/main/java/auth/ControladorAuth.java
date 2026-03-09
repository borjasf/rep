package auth;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dto.CredencialesDTO;
import modelo.Usuario;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import servicio.FactoriaServicios;
import servicio.IServicioUsuario;

@Path("auth")
public class ControladorAuth {
    
	private IServicioUsuario servicio = FactoriaServicios.getServicio(IServicioUsuario.class);
	
	
    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response login(@FormParam("username") String username, @FormParam("password") String password) throws RepositorioException, EntidadNoEncontrada {

		Map<String, Object> claims = verificarCredenciales(username, password);
		if (claims != null) {
			String token = JwtUtils.generateToken(claims);
			return Response.ok(token).build();
		} else {
			return Response.status(Response.Status.UNAUTHORIZED).entity("Credenciales inválidas").build();
		}

	}
    
private Map<String, Object> verificarCredenciales(String username, String password) throws RepositorioException, EntidadNoEncontrada {
		
		if(servicio.autenticarUsuario(username, password)) {
			HashMap<String, Object> claims = new HashMap<String, Object>();
			claims.put("sub", username);
			claims.put("roles", "PROFESOR");
		
			return claims;
	}
		return null;
		
	}
}