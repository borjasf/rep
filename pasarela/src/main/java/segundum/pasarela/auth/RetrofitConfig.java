package segundum.pasarela.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class RetrofitConfig {

    // Leemos la variable. Si no existe, usamos localhost por defecto.
    @Value("${api.usuarios.url:http://localhost:8080/api/}")
    private String baseUrl;

    @Bean
    public RetrofitUsuarios retrofit() {
    	Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl) // Usamos la variable inyectada
                .addConverterFactory(JacksonConverterFactory.create()) 
                .build();

        return retrofit.create(RetrofitUsuarios.class);
    }
}