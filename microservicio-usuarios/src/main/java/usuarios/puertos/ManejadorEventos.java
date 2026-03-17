package usuarios.puertos;

import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import servicio.FactoriaServicios;
import servicio.IServicioUsuario;


//Puerto de entrada

public class ManejadorEventos {
	
	private IServicioUsuario servicio = FactoriaServicios.getServicio(IServicioUsuario.class);
	
	// Método que será invocado cuando llegue el evento "compraventa-creada"
		public void procesarEventoCompraventaCreada(String emailComprador, String emailVendedor) throws RepositorioException, EntidadNoEncontrada {
				// Incrementamos los contadores utilizando la lógica que creamos anteriormente
				servicio.incrementarContadorCompras(emailComprador);
				servicio.incrementarContadorVentas(emailVendedor);
		}
	
}