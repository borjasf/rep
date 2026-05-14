package compraventas.adaptadores;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import compraventas.modelo.externo.UsuarioExterno;
import compraventas.servicio.IUsuariosPort;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Component
public class UsuariosAdapter implements IUsuariosPort {

	private RetrofitUsuarios api;

	public UsuariosAdapter(@Value("${usuarios.api.url}") String baseUrl) {
		// Retrofit exige que baseUrl termine en "/"
		if (!baseUrl.endsWith("/")) {
			baseUrl = baseUrl + "/";
		}
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(baseUrl)
				.addConverterFactory(JacksonConverterFactory.create())
				.build();

		this.api = retrofit.create(RetrofitUsuarios.class);
	}

	@Override
	public UsuarioExterno obtenerUsuario(String idUsuario) {
		try {
			// Ejecutamos la petición HTTP de forma síncrona
			Response<UsuarioExterno> response = api.getNombreUsuario(idUsuario).execute();
			
			if (response.isSuccessful() && response.body() != null) {
				return response.body();
			} else if (response.code() == 404) {
				throw new IllegalArgumentException("No existe un usuario con id: " + idUsuario);
			} else {
				throw new RuntimeException("Error al consultar Usuarios. HTTP " + response.code());
			}
		} catch (IOException e) {
			throw new RuntimeException("Error de conexión con el microservicio Usuarios", e);
		}
	}
}