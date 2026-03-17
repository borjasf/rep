package compraventas.adaptadores;

import org.springframework.stereotype.Component;
import compraventas.modelo.externo.UsuarioExterno;
import compraventas.servicio.IUsuariosPort;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Component 
public class UsuariosAdapter implements IUsuariosPort {

	private RetrofitUsuarios api;

	public UsuariosAdapter() {
		// Configuramos Retrofit para que apunte al puerto 8080 (donde está Usuarios)
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl("http://localhost:8080/") 
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
			} else {
				throw new RuntimeException("Usuario no encontrado o error en el servicio");
			}
		} catch (Exception e) {
			throw new RuntimeException("Error de conexión con el microservicio Usuarios", e);
		}
	}
}