package compraventas.adaptadores;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import compraventas.modelo.externo.ProductoExterno;
import compraventas.servicio.IProductosPort;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Component
public class ProductosAdapter implements IProductosPort {

	private RetrofitProductos api;

	public ProductosAdapter(@Value("${productos.api.url}") String baseUrl) {
		// Retrofit exige que baseUrl termine en "/"
		if (!baseUrl.endsWith("/")) {
			baseUrl = baseUrl + "/";
		}
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(baseUrl)
				.addConverterFactory(JacksonConverterFactory.create())
				.build();

		this.api = retrofit.create(RetrofitProductos.class);
	}

	@Override
	public ProductoExterno obtenerProducto(String idProducto) {
		try {
			Response<ProductoExterno> response = api.getProducto(idProducto).execute();
			
			if (response.isSuccessful() && response.body() != null) {
				return response.body();
			} else if (response.code() == 404) {
				throw new IllegalArgumentException("No existe un producto con id: " + idProducto);
			} else {
				throw new RuntimeException("Error al consultar Productos. HTTP " + response.code());
			}
		} catch (IOException e) {
			throw new RuntimeException("Error de conexión con el microservicio Productos", e);
		}
	}
}