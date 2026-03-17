package productos.servicio;

import modelo.Producto;
import modelo.Usuario;
import productos.dto.ProductoDTO;
import productos.repositorio.EntidadNoEncontrada;
import productos.repositorio.RepositorioException;
import productos.repositoriosAdHoc.RepositorioProductoAdHoc;
import productos.repositoriosModelo.IRepositorioCategorias;
import productos.repositoriosModelo.IRepositorioProducto;
import productos.repositoriosModelo.IRepositorioUsuario;
import modelo.Categoria;
import modelo.EstadoProducto;
import modelo.LugarRecogida;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service 
@Transactional
public class ServicioProductos implements IServiciosProductos {

	
	private IRepositorioProducto repositorioProducto;
	private IRepositorioUsuario repositorioUsuario;
	private IRepositorioCategorias repositorioCategoria;
	private RepositorioProductoAdHoc repositorioProductoAdHoc; // Para consultas complejas
	private IServiciosCategorias servicioCategorias; // Para obtener descendientes de categorías en búsqueda
	
	@Autowired
	public ServicioProductos(IRepositorioProducto repositorioProducto, IRepositorioUsuario repositorioUsuario, IRepositorioCategorias repositorioCategoria, RepositorioProductoAdHoc repositorioProductoAdHoc, IServiciosCategorias servicioCategorias) {
		this.repositorioProducto = repositorioProducto;
		this.repositorioUsuario = repositorioUsuario;
		this.repositorioCategoria = repositorioCategoria;
		this.repositorioProductoAdHoc = repositorioProductoAdHoc;
		this.servicioCategorias = servicioCategorias;
	}
	
	@Override
	public String altaProducto(String titulo, String descripcion, double precio, EstadoProducto estado,
							String idCategoria, boolean envioDisponible, String idVendedor)
							throws EntidadNoEncontrada, IllegalArgumentException {

		if (titulo == null || titulo.trim().isEmpty() || precio < 0 || estado == null || idCategoria == null || idVendedor == null) {
			 throw new IllegalArgumentException("Faltan datos obligatorios o son inválidos para dar de alta el producto.");
		}
		
		// Spring Data JPA usa Optional para las búsquedas por ID
		Usuario vendedor = repositorioUsuario.findById(idVendedor)
				.orElseThrow(() -> new EntidadNoEncontrada("Vendedor no encontrado: " + idVendedor));
		
		Categoria categoria = repositorioCategoria.findById(idCategoria)
				.orElseThrow(() -> new EntidadNoEncontrada("Categoría no encontrada: " + idCategoria));

		Producto nuevoProducto = new Producto();
		nuevoProducto.setTitulo(titulo);
		nuevoProducto.setDescripcion(descripcion);
		nuevoProducto.setPrecio(precio);
		nuevoProducto.setEstado(estado);
		nuevoProducto.setCategoria(categoria);
		nuevoProducto.setEnvioDisponible(envioDisponible);
		nuevoProducto.setVendedor(vendedor);
		nuevoProducto.setFechaPublicacion(LocalDateTime.now());
		nuevoProducto.setVisualizaciones(0);

		// Usamos save() de CrudRepository
		return repositorioProducto.save(nuevoProducto).getId();
	}

	@Override
	public void asignarLugarRecogida(String idProducto, String descripcionLugar, double longitud, double latitud)
						throws EntidadNoEncontrada, IllegalArgumentException {

		Producto producto = getProducto(idProducto); // Reutilizamos el método que ya tienes para obtener el producto o lanzar EntidadNoEncontrada

		if (descripcionLugar == null || descripcionLugar.trim().isEmpty()) {
		    throw new IllegalArgumentException("La descripción del lugar de recogida no puede estar vacía.");
		}
		LugarRecogida nuevoLugar = new LugarRecogida(descripcionLugar, longitud, latitud);
		producto.setLugarRecogida(nuevoLugar);

		repositorioProducto.save(producto);
	}

	@Override
	public void modificarProducto(String idProducto, Double nuevoPrecio, String nuevaDescripcion)
						throws EntidadNoEncontrada, IllegalArgumentException {

		Producto producto = getProducto(idProducto); // Reutilizamos el método que ya tienes para obtener el producto o lanzar EntidadNoEncontrada
		boolean modificado = false;

		if (nuevoPrecio != null) {
			if (nuevoPrecio < 0) {
				throw new IllegalArgumentException("El precio no puede ser negativo.");
			}
			producto.setPrecio(nuevoPrecio);
			modificado = true;
		}
		if (nuevaDescripcion != null) {
			producto.setDescripcion(nuevaDescripcion);
			modificado = true;
		}

		if (modificado) {
			repositorioProducto.save(producto);
		}
	}

	@Override
	public void anadirVisualizacion(String idProducto)
						throws EntidadNoEncontrada {

		Producto producto =getProducto(idProducto);
		int visualizacionesActuales = producto.getVisualizaciones();
		producto.setVisualizaciones(visualizacionesActuales + 1);
		repositorioProducto.save(producto);
	}

	//Creamos una nueva funcion que nos permitira conseguir un ProductoDTO 
	@Override
	public ProductoDTO getProductoDTO(String idProducto) throws EntidadNoEncontrada {
	   			if (idProducto == null || idProducto.isEmpty())
				throw new IllegalArgumentException("id: no debe ser nulo ni vacio");
			
			Optional<Producto> productoOpt = repositorioProducto.findById(idProducto);
			if(productoOpt.isPresent() == false) {
				throw new EntidadNoEncontrada("No se encontró el producto con id: " + idProducto);
				
			} else {
				return ProductoDTO.fromEntity(productoOpt.get());
			}
	}
	
	public Producto getProducto(String id) throws EntidadNoEncontrada{
		if (id == null || id.isEmpty())
			throw new IllegalArgumentException("id: no debe ser nulo ni vacio");
		
		Optional<Producto> productoOpt = repositorioProducto.findById(id);
		if(productoOpt.isPresent() == false) {
			throw new EntidadNoEncontrada("No se encontró el producto con id: " + id);
			
		} else {
			return productoOpt.get();
		}
	}
	    

	@Override
	public List<ProductoDTO> getProductosPorVendedor(String idVendedor) 
			throws RepositorioException, IllegalArgumentException {
		
		//  Validación de entrada
		if (idVendedor == null || idVendedor.trim().isEmpty()) {
			throw new IllegalArgumentException("El ID del vendedor no puede ser nulo o vacío.");
		}
		
		//Llamar al nuevo método del repositorio para obtener las entidades
		List<Producto> productosDelVendedor = repositorioProductoAdHoc.findByVendedorIdOrderByFechaPublicacionDesc(idVendedor);
		
		//Transformar la lista de Entidades (Producto) a una lista de DTOs (ProductoDTO)
		List<ProductoDTO> productosDTO = new ArrayList<>();
		for (Producto p : productosDelVendedor) {
			productosDTO.add(ProductoDTO.fromEntity(p)); // Reutilizamos el método que ya tienes
		}
		
		// 5. Devolver la lista de DTOs
		return productosDTO;
	}

	@Override
	public Page<ProductoResumen> getListadoPaginado(Pageable pageable) {
		return this.repositorioProducto.findAll(pageable).map(producto -> {
			ProductoResumen resumen = new ProductoResumen();
			resumen.setId(producto.getId());
			resumen.setTitulo(producto.getTitulo());
			resumen.setPrecio(producto.getPrecio());
			resumen.setEstado(producto.getEstado());
			resumen.setFechaPublicacion(producto.getFechaPublicacion());
			resumen.setEnvioDisponible(producto.isEnvioDisponible());
			if (producto.getCategoria() != null)
				resumen.setCategoriaNombre(producto.getCategoria().getDescripcion());
			return resumen;
		});
	}
	
	@Override
	public Page<ProductoDTO> historialDelMesPaginado(int mes, int anyo, Pageable paginacion) throws RepositorioException {
	    if (mes < 1 || mes > 12 || anyo <= 0) {
	        throw new IllegalArgumentException("Mes o año inválido.");
	    }
	    
	    // Llamamos al repositorio pasándole la paginación
	    Page<Producto> paginaProductos = repositorioProductoAdHoc.findProductosByMonthAndYear(mes, anyo, paginacion);
	    
	    // Usamos .map() para transformar cada Entidad en DTO automáticamente
	    return paginaProductos.map(producto -> ProductoDTO.fromEntity(producto));
	}

	@Override
	public Page<ProductoDTO> buscarProductosPaginado(String idCategoria, String textoDescripcion, EstadoProducto estadoMinimo, Double precioMax, Pageable paginacion) throws RepositorioException, EntidadNoEncontrada { 

	    List<String> idsCategoriasParaBuscar = null;

	    if (idCategoria != null && !idCategoria.trim().isEmpty()) {
	        idsCategoriasParaBuscar = new ArrayList<>();

	        Categoria catRaiz = repositorioCategoria.findById(idCategoria)
	                .orElseThrow(() -> new EntidadNoEncontrada("Categoría no encontrada: " + idCategoria));
	        
	        idsCategoriasParaBuscar.add(catRaiz.getId());

	        List<Categoria> descendientes = servicioCategorias.recuperarTodosDescendientes(idCategoria); 

	        if (descendientes != null) {
	            for (Categoria cat : descendientes) {
	                if (!idsCategoriasParaBuscar.contains(cat.getId())) {
	                    idsCategoriasParaBuscar.add(cat.getId());
	                }
	            }
	        }
	    }

	    // Llamamos al repositorio pasándole la paginación
	    Page<Producto> paginaProductos = repositorioProductoAdHoc.findProductosByCriteria(
	            idsCategoriasParaBuscar, textoDescripcion, estadoMinimo, precioMax, paginacion);
	    
	    // Convertimos la página de entidades a página de DTOs
	    return paginaProductos.map(producto -> ProductoDTO.fromEntity(producto));
	}
	
	
	@Override
	public void marcarComoVendido(String idProducto) throws EntidadNoEncontrada {
		// Reutilizamos tu método getProducto que ya busca y lanza la excepción si no existe
		Producto producto = getProducto(idProducto);
		
		// Cambiamos el estado y guardamos
		producto.setVendido(true);
		repositorioProducto.save(producto);
	}
}