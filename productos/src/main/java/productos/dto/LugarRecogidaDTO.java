package productos.dto;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
@Schema(description = "Lugar de recogida del producto")
public class LugarRecogidaDTO {

    @NotBlank
    @Schema(description = "Descripción del lugar de recogida", example = "Calle Falsa 123, Springfield")
    private String descripcion;

    @DecimalMin(value = "-180.0", message = "La longitud debe estar entre -180 y 180")
    @DecimalMax(value = "180.0", message = "La longitud debe estar entre -180 y 180")
    @Schema(description = "Longitud del lugar de recogida", example = "-3.703790")
    private double longitud;
    
    @DecimalMin(value = "-90.0", message = "La latitud debe estar entre -90 y 90")
    @DecimalMax(value = "90.0", message = "La latitud debe estar entre -90 y 90")
    @Schema(description = "Latitud del lugar de recogida", example = "40.416775")
    private double latitud;

    //POJO
    public LugarRecogidaDTO() {
    }
    
    public LugarRecogidaDTO(String descripcion, double longitud, double latitud) {
        this.descripcion = descripcion;
        this.longitud = longitud;
        this.latitud = latitud;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getLongitud() {
        return longitud;
    }

    public double getLatitud() {
        return latitud;
    }
}