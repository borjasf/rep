package compraventas.adaptadores;

import compraventas.modelo.externo.UsuarioExterno;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitUsuarios {
	// LLamada a la "puerta" abierta en la tarea 5.
	@GET("usuarios/{id}/nombre") //NO SE SI HAY QUE PONER /api/usuarios/{id}/nombre
	Call<UsuarioExterno> getNombreUsuario(@Path("id") String id);
}