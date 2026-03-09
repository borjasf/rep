package productos.rest;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import modelo.EstadoProducto;
import productos.dto.LugarRecogidaDTO;
import productos.dto.ModificarProductoDTO;
import productos.dto.NuevoProductoDTO;
import productos.dto.ProductoDTO;
import productos.servicio.IServiciosProductos;
import productos.servicio.ProductoResumen;

@RestController
@RequestMapping("/productos")
public class ProductosController implements  ProductosApi {

	private IServiciosProductos servicio;
	
	@Autowired
	private PagedResourcesAssembler<ProductoResumen> pagedResourcesAssembler;
	
	@Autowired
	private PagedResourcesAssembler<ProductoDTO> pagedResourcesAssemblerDTO;
	
	@Autowired
	private ProductosResumenAssembler productosResumenAssembler;
	
	@Autowired
	private ProductoDTOAssembler productoDTOAssembler;
	
	@Autowired
	public ProductosController(IServiciosProductos servicio) {
		this.servicio = servicio;
	}
	
	@PostMapping
	public ResponseEntity<Void> darAltaProducto(
			 @Valid @RequestBody NuevoProductoDTO nuevoProducto) throws Exception {
		 
		String id = this.servicio.altaProducto(nuevoProducto.getTitulo(),
				nuevoProducto.getDescripcion(),
				nuevoProducto.getPrecio(),
				nuevoProducto.getEstado(),
				nuevoProducto.getIdCategoria(), 
				nuevoProducto.isEnvioDisponible(), 
				nuevoProducto.getIdVendedor());
	 
		 // Construye la URL completa del nuevo recurso
		 URI nuevaURL = ServletUriComponentsBuilder.fromCurrentRequest()
				 			.path("/{id}").buildAndExpand(id).toUri();
		 
		 return ResponseEntity.created(nuevaURL).build();
	}

	@GetMapping("/{id}")
	public EntityModel<ProductoDTO> getProductoById(@PathVariable String id) throws Exception {
		ProductoDTO producto = this.servicio.getProductoDTO(id);
		
		//Envolver el DTO en un EntityModel y agregar enlaces self
		EntityModel<ProductoDTO> model = EntityModel.of(producto);
		model.add( WebMvcLinkBuilder.linkTo(
					 WebMvcLinkBuilder
					 	.methodOn(ProductosController.class)
					 	.getProductoById(id))
				 .withSelfRel());
		return model;
	}

	@PatchMapping("/{id}/recogida")
	public ResponseEntity<Void> asignarLugarRecogida(@PathVariable String id,
			@RequestBody LugarRecogidaDTO lugarRecogida) throws Exception {
		this.servicio.asignarLugarRecogida(id, lugarRecogida.getDescripcion(),
				lugarRecogida.getLongitud(), lugarRecogida.getLatitud());
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Void> modificarProducto(@PathVariable String id,
			@RequestBody ModificarProductoDTO productoModificado) throws Exception {
		this.servicio.modificarProducto(id, productoModificado.getPrecio(), productoModificado.getDescripcion());
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/{id}/visualizaciones")
	public ResponseEntity<Void> aniadirVisualizacion(@PathVariable String id) throws Exception {
		this.servicio.anadirVisualizacion(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/historial/anyo/{anyo}/mes/{mes}")
	public PagedModel<EntityModel<ProductoDTO>> historialMes(@PathVariable int anyo, @PathVariable int mes, Pageable paginacion) throws Exception {
		Page<ProductoDTO> resultado = this.servicio.historialDelMesPaginado(mes, anyo, paginacion);
		return this.pagedResourcesAssemblerDTO.toModel(resultado, productoDTOAssembler);
	}

	@GetMapping("/buscar")
	public PagedModel<EntityModel<ProductoDTO>> buscarProductosVenta(
			@RequestParam(required = false) String categoria,
			@RequestParam(required = false) String texto,
			@RequestParam(required = false) EstadoProducto estado,
			@RequestParam(required = false) Double precioMax,
			Pageable paginacion) throws Exception {
		Page<ProductoDTO> resultado = this.servicio.buscarProductosPaginado(categoria, texto, estado, precioMax, paginacion);
		return this.pagedResourcesAssemblerDTO.toModel(resultado, productoDTOAssembler);
	}

	@GetMapping
	public PagedModel<EntityModel<ProductoResumen>> getProductosPaginado(Pageable paginacion) throws Exception {
		Page<ProductoResumen> resultado = this.servicio.getListadoPaginado(paginacion);
		return this.pagedResourcesAssembler.toModel(resultado, productosResumenAssembler);
	}
}
