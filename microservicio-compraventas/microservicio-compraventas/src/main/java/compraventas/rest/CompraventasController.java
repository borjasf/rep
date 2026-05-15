package compraventas.rest;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import compraventas.modelo.Compraventa;
import compraventas.rest.dto.CompraventaDTO;
import compraventas.rest.dto.NuevaCompraventaDTO;
import compraventas.servicio.IServicioCompraventas;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Compraventas", description = "Operaciones de compraventa entre usuarios")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/compraventas")
public class CompraventasController {

	private IServicioCompraventas servicio;
	
	@Autowired
	private PagedResourcesAssembler<Compraventa> pagedAssembler;
	
	@Autowired
	private CompraventaDTOAssembler dtoAssembler;

	@Autowired
	public CompraventasController(IServicioCompraventas servicio) {
		this.servicio = servicio;
	}

	// Regla 1: Rol USUARIO y que el usuario solicitante sea el comprador
	@Operation(summary = "Registrar una nueva compraventa",
			description = "Crea una compraventa asociada a un producto y a un comprador. Solo el propio comprador (rol USUARIO) puede invocarlo.")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Compraventa creada correctamente. La cabecera Location contiene la URI del nuevo recurso."),
			@ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
			@ApiResponse(responseCode = "401", description = "Falta autenticación o JWT inválido"),
			@ApiResponse(responseCode = "403", description = "El usuario autenticado no coincide con el comprador"),
			@ApiResponse(responseCode = "404", description = "El producto o el comprador no existen")
	})
	@PreAuthorize("hasRole('USER') and principal.equals(#dto.idComprador)")
	@PostMapping
	public ResponseEntity<Void> registrarCompraventa(@Valid @RequestBody NuevaCompraventaDTO dto) {
		String id = servicio.registrarCompraventa(dto.getIdProducto(), dto.getIdComprador());

		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(id)
				.toUri();

		return ResponseEntity.created(location).build();
	}

	// (Auxiliar HATEOAS: Se deja accesible para usuarios autenticados)
	@Operation(summary = "Consultar una compraventa por su id",
			description = "Devuelve la compraventa identificada por su id con enlaces HATEOAS asociados.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Compraventa encontrada"),
			@ApiResponse(responseCode = "401", description = "Falta autenticación"),
			@ApiResponse(responseCode = "404", description = "No existe una compraventa con ese id")
	})
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/{id}")
	public EntityModel<CompraventaDTO> getCompraventa(
			@Parameter(description = "Identificador único de la compraventa", required = true, example = "65f8a1b3c2d4e5f6a7b8c9d0")
			@PathVariable String id) {
		Compraventa cv = servicio.getCompraventa(id);
		return dtoAssembler.toModel(cv);
	}

	// Regla 2: Rol USUARIO y que coincida con el usuario de la consulta
	@Operation(summary = "Listar las compras de un usuario",
			description = "Devuelve, paginadas, las compraventas en las que el usuario indicado figura como comprador. Solo el propio usuario puede consultarlas.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Página de compras del usuario"),
			@ApiResponse(responseCode = "401", description = "Falta autenticación"),
			@ApiResponse(responseCode = "403", description = "El usuario autenticado no coincide con idComprador")
	})
	@PreAuthorize("hasRole('USER') and principal.equals(#idComprador)")
	@GetMapping("/compras/{idComprador}")
	public PagedModel<EntityModel<CompraventaDTO>> getComprasUsuario(
			@Parameter(description = "Identificador del usuario comprador", required = true, example = "user-42")
			@PathVariable String idComprador,
			@Parameter(description = "Parámetros de paginación (page, size, sort)")
			Pageable pageable) {

		Page<Compraventa> compras = servicio.getComprasUsuario(idComprador, pageable);
		return pagedAssembler.toModel(compras, dtoAssembler);
	}

	// Regla 3: Rol USUARIO y que coincida con el vendedor de la consulta
	@Operation(summary = "Listar las ventas de un usuario",
			description = "Devuelve, paginadas, las compraventas en las que el usuario indicado figura como vendedor. Solo el propio usuario puede consultarlas.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Página de ventas del usuario"),
			@ApiResponse(responseCode = "401", description = "Falta autenticación"),
			@ApiResponse(responseCode = "403", description = "El usuario autenticado no coincide con idVendedor")
	})
	@PreAuthorize("hasRole('USER') and principal.equals(#idVendedor)")
	@GetMapping("/ventas/{idVendedor}")
	public PagedModel<EntityModel<CompraventaDTO>> getVentasUsuario(
			@Parameter(description = "Identificador del usuario vendedor", required = true, example = "user-7")
			@PathVariable String idVendedor,
			@Parameter(description = "Parámetros de paginación (page, size, sort)")
			Pageable pageable) {

		Page<Compraventa> ventas = servicio.getVentasUsuario(idVendedor, pageable);
		return pagedAssembler.toModel(ventas, dtoAssembler);
	}

	// Regla 4: Solo ADMINISTRADOR puede ver compraventas entre dos usuarios específicos
	@Operation(summary = "Listar transacciones entre dos usuarios (admin)",
			description = "Devuelve, paginadas, las compraventas en las que figuran como comprador y vendedor los usuarios indicados. Operación restringida al rol ADMINISTRADOR.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Página de transacciones encontradas"),
			@ApiResponse(responseCode = "401", description = "Falta autenticación"),
			@ApiResponse(responseCode = "403", description = "El usuario autenticado no tiene rol ADMINISTRADOR")
	})
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public PagedModel<EntityModel<CompraventaDTO>> getTransacciones(
			@Parameter(description = "Identificador del comprador", required = true, example = "user-42")
			@RequestParam String idComprador,
			@Parameter(description = "Identificador del vendedor", required = true, example = "user-7")
			@RequestParam String idVendedor,
			@Parameter(description = "Parámetros de paginación (page, size, sort)")
			Pageable pageable) {

		Page<Compraventa> transacciones = servicio.getCompraventas(idComprador, idVendedor, pageable);
		return pagedAssembler.toModel(transacciones, dtoAssembler);
	}
}