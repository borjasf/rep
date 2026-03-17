package compraventas.servicio;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import compraventas.modelo.Compraventa;
import compraventas.modelo.externo.ProductoExterno;
import compraventas.modelo.externo.UsuarioExterno;
import compraventas.repositorio.RepositorioCompraventas;

@Service 
public class ServicioCompraventas implements IServicioCompraventas {

	private RepositorioCompraventas repositorio;
	private IProductosPort productosPort;
	private IUsuariosPort usuariosPort;

	@Autowired
	public ServicioCompraventas(RepositorioCompraventas repositorio, 
								IProductosPort productosPort,
								IUsuariosPort usuariosPort) {
		this.repositorio = repositorio;
		this.productosPort = productosPort;
		this.usuariosPort = usuariosPort;
	}

	@Override
	public String registrarCompraventa(String idProducto, String idComprador) {
		
		ProductoExterno producto = productosPort.obtenerProducto(idProducto);

		UsuarioExterno comprador = usuariosPort.obtenerUsuario(idComprador);

		UsuarioExterno vendedor = usuariosPort.obtenerUsuario(producto.getVendedor());

		// TODO (Nota 2 del enunciado): En una tarea posterior habrá que comprobar que el producto no esté ya vendido.

		Compraventa cv = new Compraventa();
		
		cv.setIdProducto(idProducto);
		cv.setTitulo(producto.getTitulo());
		cv.setPrecio(producto.getPrecio());
		
		// El enunciado dice: "la información con la recogida se almacena de forma simplificada como cadena de texto"
		cv.setRecogida(producto.getLugarRecogida() != null ? producto.getLugarRecogida().toString() : "No especificado");

		cv.setIdComprador(idComprador);
		cv.setNombreComprador(comprador.getNombre() + " " + comprador.getApellidos());

		cv.setIdVendedor(producto.getVendedor());
		cv.setNombreVendedor(vendedor.getNombre() + " " + vendedor.getApellidos());

		cv.setFecha(LocalDateTime.now());

		Compraventa guardada = repositorio.save(cv);

		return guardada.getId();
	}

	@Override
	public Compraventa getCompraventa(String id) {
		return repositorio.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Compraventa no encontrada"));
	}

	@Override
	public Page<Compraventa> getComprasUsuario(String idComprador, Pageable pageable) {
		return repositorio.findByIdComprador(idComprador, pageable);
	}

	@Override
	public Page<Compraventa> getVentasUsuario(String idVendedor, Pageable pageable) {
		return repositorio.findByIdVendedor(idVendedor, pageable);
	}

	@Override
	public Page<Compraventa> getCompraventas(String idComprador, String idVendedor, Pageable pageable) {
		return repositorio.findByIdCompradorAndIdVendedor(idComprador, idVendedor, pageable);
	}
}