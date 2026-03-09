package modelo;

import java.time.LocalDate;
import repositorio.Identificable;
import javax.persistence.*;

@Entity
public class Usuario implements Identificable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private String id;
	
	private String email;
	private String nombre;
	private String apellidos; 
	
	@Lob
	private String clave;
	
	private LocalDate fechaNacimiento;
	private String telefono;
	private boolean esAdmin;
	
	public Usuario() {	
	}
	
	public Usuario(String email, String nombre, String apellidos, String clave, LocalDate fechaNacimiento, String telefono, boolean esAdmin) {
		this.email = email;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.clave = clave;
		this.fechaNacimiento = fechaNacimiento;
		this.telefono = telefono;
		this.esAdmin = esAdmin;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public boolean isAdmin() {
		return esAdmin;
	}

	public void setAdmin(boolean esAdmin) {
		this.esAdmin = esAdmin;
	}
	
	
	
}
