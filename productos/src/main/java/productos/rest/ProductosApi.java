package productos.rest;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import modelo.Categoria;
import modelo.EstadoProducto;
import productos.dto.LugarRecogidaDTO;
import productos.dto.ModificarProductoDTO;
import productos.dto.NuevoProductoDTO;
import productos.dto.ProductoDTO;
import productos.servicio.ProductoResumen;

public interface ProductosApi {

	@Operation(summary = "Obtener producto", description = "Obtiene un producto por su id")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Producto encontrado"),
			@ApiResponse(responseCode = "404", description = "No existe un producto con ese id")
	})
	@GetMapping("/{id}")
	EntityModel<ProductoDTO> getProductoById(
			@Parameter(description = "Identificador del producto", required = true, example = "42")
			@PathVariable String id) throws Exception;

	@Operation(summary = "Obtener categorías", description = "Obtiene todas las categorías disponibles")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Lista de categorías devuelta correctamente")
	})
	@GetMapping("/categorias")
	ResponseEntity<List<Categoria>> getCategorias() throws Exception;

	@Operation(summary = "Dar de alta producto",
			description = "Da de alta un nuevo producto. Solo el propio vendedor (rol USUARIO) puede crearlo. El producto se crea sin lugar de recogida.")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Producto creado. La cabecera Location contiene la URI del nuevo recurso."),
			@ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
			@ApiResponse(responseCode = "401", description = "Falta autenticación o JWT inválido"),
			@ApiResponse(responseCode = "403", description = "El usuario autenticado no coincide con idVendedor"),
			@ApiResponse(responseCode = "404", description = "La categoría o el vendedor no existen")
	})
	@PostMapping
	ResponseEntity<Void> darAltaProducto(@Valid @RequestBody NuevoProductoDTO nuevoProducto) throws Exception;

	@Operation(summary = "Asignar lugar de recogida", description = "Asigna un lugar de recogida a un producto existente")
	@ApiResponses({
			@ApiResponse(responseCode = "204", description = "Lugar de recogida asignado correctamente"),
			@ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
			@ApiResponse(responseCode = "401", description = "Falta autenticación"),
			@ApiResponse(responseCode = "403", description = "El usuario autenticado no es el propietario del producto"),
			@ApiResponse(responseCode = "404", description = "No existe un producto con ese id")
	})
	@PatchMapping("/{id}/recogida")
	ResponseEntity<Void> asignarLugarRecogida(
			@Parameter(description = "Identificador del producto", required = true, example = "42")
			@PathVariable String id,
			@Valid @RequestBody LugarRecogidaDTO lugarRecogida) throws Exception;

	@Operation(summary = "Modificar producto",
			description = "Modifica parcialmente el precio y/o la descripción de un producto. Solo el propietario puede invocarlo.")
	@ApiResponses({
			@ApiResponse(responseCode = "204", description = "Producto modificado correctamente"),
			@ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
			@ApiResponse(responseCode = "401", description = "Falta autenticación"),
			@ApiResponse(responseCode = "403", description = "El usuario autenticado no es el propietario"),
			@ApiResponse(responseCode = "404", description = "No existe un producto con ese id")
	})
	@PatchMapping("/{id}")
	ResponseEntity<Void> modificarProducto(
			@Parameter(description = "Identificador del producto", required = true, example = "42")
			@PathVariable String id,
			@Valid @RequestBody ModificarProductoDTO productoModificado) throws Exception;

	@Operation(summary = "Añadir visualización", description = "Incrementa en uno las visualizaciones de un producto")
	@ApiResponses({
			@ApiResponse(responseCode = "204", description = "Visualización añadida"),
			@ApiResponse(responseCode = "404", description = "No existe un producto con ese id")
	})
	@PatchMapping("/{id}/visualizaciones")
	ResponseEntity<Void> aniadirVisualizacion(
			@Parameter(description = "Identificador del producto", required = true, example = "42")
			@PathVariable String id) throws Exception;

	@Operation(summary = "Historial del mes",
			description = "Obtiene los productos publicados en un mes y año concretos, paginados con enlaces HATEOAS.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Página de productos del mes indicado"),
			@ApiResponse(responseCode = "400", description = "Mes o año fuera de rango")
	})
	@GetMapping("/historial/anyo/{anyo}/mes/{mes}")
	PagedModel<EntityModel<ProductoDTO>> historialMes(
			@Parameter(description = "Año del histórico", required = true, example = "2026")
			@PathVariable int anyo,
			@Parameter(description = "Mes del histórico (1-12)", required = true, example = "5")
			@PathVariable int mes,
			@Parameter(description = "Parámetros de paginación (page, size, sort)")
			Pageable paginacion) throws Exception;

	@Operation(summary = "Buscar productos en venta",
			description = "Busca productos disponibles para la venta combinando filtros opcionales de categoría, texto, estado y precio máximo.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Página de productos que cumplen los filtros")
	})
	@GetMapping("/buscar")
	PagedModel<EntityModel<ProductoDTO>> buscarProductosVenta(
			@Parameter(description = "Nombre de la categoría a filtrar", example = "Electrónica")
			@RequestParam(required = false) String categoria,
			@Parameter(description = "Texto a buscar en título/descripción", example = "bicicleta")
			@RequestParam(required = false) String texto,
			@Parameter(description = "Estado del producto")
			@RequestParam(required = false) EstadoProducto estado,
			@Parameter(description = "Precio máximo", example = "200.0")
			@RequestParam(required = false) Double precioMax,
			@Parameter(description = "Parámetros de paginación (page, size, sort)")
			Pageable paginacion) throws Exception;

	@Operation(summary = "Listado paginado", description = "Obtiene un listado paginado de productos con enlaces HATEOAS")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Página de productos")
	})
	@GetMapping
	PagedModel<EntityModel<ProductoResumen>> getProductosPaginado(
			@Parameter(description = "Parámetros de paginación (page, size, sort)")
			Pageable paginacion) throws Exception;
}
