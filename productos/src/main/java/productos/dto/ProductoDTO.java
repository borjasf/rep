package productos.dto;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import modelo.EstadoProducto;
import modelo.LugarRecogida;
import modelo.Producto;

@Schema(description = "DTO de la entidad Encuesta")
public class ProductoDTO{

	@Schema(description = "Identificador dedel producto")
	private String id;
	@Schema(description = "Título del producto")
	private String titulo; 
	@Schema(description = "Descripción del producto")
	private String descripcion;
	@Schema(description = "Precio del producto")
	private double precio;
	@Schema(description = "Estado del producto")
	private EstadoProducto estado;
	@Schema(description = "Fecha de publicación del producto")
	private LocalDateTime fechaPublicacion;
	@Schema(description = "Número de visualizaciones del producto")
	private int visualizaciones;
	@Schema(description = "Indica si el envío está disponible para el producto")
	private boolean envioDisponible;
	@Schema(description = "Lugar de recogida del producto")
    private LugarRecogida lugarRecogida;
	@Schema(description = "Nombre de la categoría del producto")
    private String nombreCategoria;
	@Schema(description = "Nombre del vendedor del producto")
    private String nombreVendedor;
    
	//POJO
	public ProductoDTO() {
	}
	
	public ProductoDTO(String id, String titulo, String descripcion, double precio, EstadoProducto estado,
			LocalDateTime fechaPublicacion, int visualizaciones, boolean envioDisponible,
			LugarRecogida lugarRecogida, String nombreCategoria, String nombreVendedor) {
		this.id = id;
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.precio = precio;
		this.estado = estado;
		this.fechaPublicacion = fechaPublicacion;
		this.visualizaciones = visualizaciones;
		this.envioDisponible = envioDisponible;
		this.lugarRecogida = lugarRecogida;
		this.nombreCategoria = nombreCategoria;
		this.nombreVendedor = nombreVendedor;
	}
    	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public LocalDateTime getFechaPublicacion() {
		return fechaPublicacion;
	}

	public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
		this.fechaPublicacion = fechaPublicacion;
	}

	public int getVisualizaciones() {
		return visualizaciones;
	}

	public void setVisualizaciones(int visualizaciones) {
		this.visualizaciones = visualizaciones;
	}

	public boolean isEnvioDisponible() {
		return envioDisponible;
	}

	public void setEnvioDisponible(boolean envioDisponible) {
		this.envioDisponible = envioDisponible;
	}

	public LugarRecogida getLugarRecogida() {
		return lugarRecogida;
	}

	public void setLugarRecogida(LugarRecogida lugarRecogida) {
		this.lugarRecogida = lugarRecogida;
	}

	
	public String getNombreCategoria() {
        return nombreCategoria;
    }
    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getNombreVendedor() {
        return nombreVendedor;
    }
    public void setNombreVendedor(String nombreVendedor) {
        this.nombreVendedor = nombreVendedor;
    }
    
    public static ProductoDTO fromEntity(Producto producto) {
		return new ProductoDTO(
			producto.getId(),
			producto.getTitulo(),
			producto.getDescripcion(),
			producto.getPrecio(),
			producto.getEstado(),
			producto.getFechaPublicacion(),
			producto.getVisualizaciones(),
			producto.isEnvioDisponible(),
			producto.getLugarRecogida(),
			producto.getCategoria().getNombre(),
			producto.getVendedor().getNombre()
		);
	}
	
}