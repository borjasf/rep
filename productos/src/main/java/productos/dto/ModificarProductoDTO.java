package productos.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para modificar un producto existente. Todos los campos son opcionales: solo se modifica lo que se envía.")
public class ModificarProductoDTO{

    @DecimalMin(value = "0.0", inclusive = true, message = "El precio no puede ser negativo.")
    @Schema(description = "Nuevo precio del producto", example = "19.99")
    private Double precio;
    
    @Schema(description = "Nueva descripción del producto", example = "Camiseta de algodón 100%")
    private String descripcion;

    //POJO 
    public ModificarProductoDTO() {
    }

    public ModificarProductoDTO(Double precio, String descripcion) {
        this.precio = precio;
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


}