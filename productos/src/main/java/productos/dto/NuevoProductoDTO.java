package productos.dto;

import modelo.EstadoProducto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para crear un nuevo producto")
public class NuevoProductoDTO {
	//Ahora ponemos aquellas que no puedan ser nulas con @NotBlank/@NotNull según tipo
	@NotBlank
	@Schema(description = "Título del nuevo producto", example = "Camiseta de algodón")
	private String titulo;

	@Schema(description = "Descripción del nuevo producto", example = "Camiseta de algodón 100%")
	private String descripcion;

	@NotNull
	@DecimalMin(value = "0.0", inclusive = true, message = "El precio no puede ser negativo.")
	@Schema(description = "Precio del nuevo producto", example = "19.99")
	private double precio;

	@NotNull
	@Schema(description = "Estado del nuevo producto")
	private EstadoProducto estado;

	@NotBlank
	@Schema(description = "ID de la categoría del nuevo producto")
	private String idCategoria;

	@Schema(description = "Indica si el envío está disponible para el nuevo producto", example = "true")
	private boolean envioDisponible;

	@NotBlank
	@Schema(description = "ID del vendedor que crea el nuevo producto")
	private String idVendedor;
	
	//POJO 
	public NuevoProductoDTO() {
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public EstadoProducto getEstado() {
		return estado;
	}

	public void setEstado(EstadoProducto estado) {
		this.estado = estado;
	}

	public String getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(String idCategoria) {
		this.idCategoria = idCategoria;
	}

	public boolean isEnvioDisponible() {
		return envioDisponible;
	}

	public void setEnvioDisponible(boolean envioDisponible) {
		this.envioDisponible = envioDisponible;
	}

	public String getIdVendedor() {
		return idVendedor;
	}

	public void setIdVendedor(String idVendedor) {
		this.idVendedor = idVendedor;
	}
	
	
	
}
