package segundum.pasarela.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import segundum.pasarela.dto.UsuarioDTO;
import retrofit2.Response;

import javax.servlet.http.Cookie;

@Component
public class SecuritySuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	private RetrofitUsuarios retrofitUsuarios;
	private static final int JWT_TIEMPO_VALIDEZ = 60 * 60; // 1 hora en segundos

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {

		DefaultOAuth2User usuario = (DefaultOAuth2User) authentication.getPrincipal();

		// Cogemos el id y el email de GitHub del usuario autenticado.
		String githubId = String.valueOf(usuario.getAttributes().get("id"));
		System.out.println("=== ATRIBUTOS DE GITHUB ===");
		usuario.getAttributes().forEach((k, v) -> System.out.println(k + " = " + v));
		System.out.println("===========================");
		String emailGitHub = String.valueOf(usuario.getAttributes().get("email"));

		try {
		    Response<UsuarioDTO> respuestaRetrofit = retrofitUsuarios.buscarUsuarioPorGitHub(githubId).execute();
		    UsuarioDTO usuarioDTO = null;

		    if (respuestaRetrofit.isSuccessful() && respuestaRetrofit.body() != null) {
		        // Usuario ya tiene githubId vinculado
		        usuarioDTO = respuestaRetrofit.body();
		    } else if (emailGitHub != null && !emailGitHub.equals("null")) {
		        // Intentamos buscarlo por email y vincularlo automáticamente
		        Response<UsuarioDTO> respuestaEmail = retrofitUsuarios.buscarUsuarioPorEmail(emailGitHub).execute();
		        if (respuestaEmail.isSuccessful() && respuestaEmail.body() != null) {
		            usuarioDTO = respuestaEmail.body();
		            // Vinculamos el githubId a este usuario automáticamente
		            retrofitUsuarios.vincularGitHub(usuarioDTO.getId(), githubId).execute();
		            System.out.println("GitHub ID vinculado automáticamente al usuario: " + usuarioDTO.getNombre());
		        }
		    }

		    if (usuarioDTO != null) {
		        Map<String, Object> claims = new HashMap<>();
		        claims.put("sub", usuarioDTO.getId());
		        claims.put("roles", usuarioDTO.isAdmin() ? "ADMIN" : "USER");
		        String jwt = JwtUtils.generateToken(claims);

		        Cookie cookie = new Cookie("jwt", jwt);
		        cookie.setMaxAge(JWT_TIEMPO_VALIDEZ);
		        cookie.setHttpOnly(true);
		        cookie.setPath("/");
		        response.addCookie(cookie);

		        response.setContentType("application/json");
		        response.setCharacterEncoding("UTF-8");
		        String jsonResponse = String.format(
		            "{\"token\":\"%s\", \"identificador\":\"%s\", \"nombre\":\"%s\", \"roles\":\"%s\"}",
		            jwt, usuarioDTO.getId(), usuarioDTO.getNombre(),
		            usuarioDTO.isAdmin() ? "ADMIN" : "USER");
		        response.getWriter().write(jsonResponse);
		    } else {
		        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
		            "Usuario de GitHub no registrado en el sistema");
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al comunicar con el MS Usuarios");
		}
	}

}