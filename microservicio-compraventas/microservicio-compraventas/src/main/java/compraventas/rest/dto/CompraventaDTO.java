package compraventas.rest.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representación de una compraventa registrada en el sistema")
public class CompraventaDTO {

	@Schema(description = "Identificador único de la compraventa", example = "65f8a1b3c2d4e5f6a7b8c9d0")
	private String id;
	@Schema(description = "Identificador del producto vendido", example = "prod-123")
	private String idProducto;
	@Schema(description = "Título del producto en el momento de la compra", example = "Bicicleta de montaña")
	private String titulo;
	@Schema(description = "Precio pagado por el producto", example = "150.00")
	private double precio;
	@Schema(description = "Lugar de recogida acordado", example = "Murcia centro")
	private String recogida;
	@Schema(description = "Identificador del usuario vendedor", example = "user-7")
	private String idVendedor;
	@Schema(description = "Nombre completo del vendedor", example = "Alejandro Carrión")
	private String nombreVendedor;
	@Schema(description = "Identificador del usuario comprador", example = "user-42")
	private String idComprador;
	@Schema(description = "Nombre completo del comprador", example = "Borja Sancho")
	private String nombreComprador;
	@Schema(description = "Fecha y hora de la transacción", example = "2026-05-14T12:30:00")
	private LocalDateTime fecha;

	public CompraventaDTO() {}

	public String getId() { return id; }
	public void setId(String id) { this.id = id; }
	
	public String getIdProducto() { return idProducto; }
	public void setIdProducto(String idProducto) { this.idProducto = idProducto; }
	
	public String getTitulo() { return titulo; }
	public void setTitulo(String titulo) { this.titulo = titulo; }
	
	public double getPrecio() { return precio; }
	public void setPrecio(double precio) { this.precio = precio; }
	
	public String getRecogida() { return recogida; }
	public void setRecogida(String recogida) { this.recogida = recogida; }
	
	public String getIdVendedor() { return idVendedor; }
	public void setIdVendedor(String idVendedor) { this.idVendedor = idVendedor; }
	
	public String getNombreVendedor() { return nombreVendedor; }
	public void setNombreVendedor(String nombreVendedor) { this.nombreVendedor = nombreVendedor; }
	
	public String getIdComprador() { return idComprador; }
	public void setIdComprador(String idComprador) { this.idComprador = idComprador; }
	
	public String getNombreComprador() { return nombreComprador; }
	public void setNombreComprador(String nombreComprador) { this.nombreComprador = nombreComprador; }
	
	public LocalDateTime getFecha() { return fecha; }
	public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}