package compraventas.adaptadores;

import org.springframework.stereotype.Component;
import compraventas.modelo.externo.ProductoExterno;
import compraventas.servicio.IProductosPort;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Component
public class ProductosAdapter implements IProductosPort {

	private RetrofitProductos api;

	public ProductosAdapter() {
		// Configuramos Retrofit para que apunte al puerto 8081 (donde está Productos)
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl("http://localhost:8083/") 
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
			} else {
				throw new RuntimeException("Producto no encontrado");
			}
		} catch (Exception e) {
			throw new RuntimeException("Error de conexión con el microservicio Productos", e);
		}
	}
}