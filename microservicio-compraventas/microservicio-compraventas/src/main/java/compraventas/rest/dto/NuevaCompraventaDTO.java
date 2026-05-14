package compraventas.rest.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos necesarios para registrar una nueva compraventa")
public class NuevaCompraventaDTO {

	@Schema(description = "Identificador del producto que se compra", example = "65f8a1b3c2d4e5f6a7b8c9d0", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotNull(message = "El id del producto no puede ser nulo")
	@NotBlank(message = "El id del producto no puede estar vacío")
	private String idProducto;

	@Schema(description = "Identificador del usuario comprador", example = "user-42", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotNull(message = "El id del comprador no puede ser nulo")
	@NotBlank(message = "El id del comprador no puede estar vacío")
	private String idComprador;

	public NuevaCompraventaDTO() {}

	public String getIdProducto() { return idProducto; }
	public void setIdProducto(String idProducto) { this.idProducto = idProducto; }
	public String getIdComprador() { return idComprador; }
	public void setIdComprador(String idComprador) { this.idComprador = idComprador; }
}