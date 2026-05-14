package segundum.pasarela.auth;


import segundum.pasarela.dto.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitUsuarios {
    
    @POST("usuarios/verificar") // <-- Cambio a POST
    Call<UsuarioDTO> verificarCredenciales(@Body CredencialesDTO credenciales); 
    
    //Hacemos un nuevo método para buscar el ususario por el id de GitHub
    @GET("usuarios")
    Call<UsuarioDTO> buscarUsuarioPorGitHub(@Query("githubId") String githubId);
    
    @GET("usuarios")
    Call<UsuarioDTO> buscarUsuarioPorEmail(@Query("email") String email);
    
    @POST("usuarios/{id}/vincularGitHub")
    Call<Void> vincularGitHub(@Path("id") String id, @Query("githubId") String githubId);
    
}