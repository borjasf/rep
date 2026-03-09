package servicio;

import java.time.LocalDate;
import java.util.List;

import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import modelo.Usuario;

public interface IServicioUsuario {

	// Funcionalidad: Registrar un nuevo usuario (donde telefono es opcional)
	String registrarUsuario(String nombre, String apellidos, String email, String clave, LocalDate fechaNacimiento,
			String telefono, boolean admin) throws RepositorioException;

	// Funcionalidad: Permitir que un usuario existente pueda cambiar sus datos para
	// poder mantener su informacion actualizada
	void actualizarDatosUsuario(String idUsuario, String nombre, String apellidos, String email, String clave,
			LocalDate fechaNacimiento, String telefono) throws RepositorioException, EntidadNoEncontrada;

	/*
	 * //Funcionalidad: Permitir a un usuario con privilegios de administrador
	 * modificar la descripcion de una categoria existente void
	 * modificarCategoria(String idUsuario, String idCategoria, String descripcion)
	 * throws RepositorioException, EntidadNoEncontrada;
	 */

	void asignarRolAdmin(String idUsuario2) throws RepositorioException, EntidadNoEncontrada;

	Usuario iniciarSesion(String email, String clave) throws RepositorioException, EntidadNoEncontrada;

	// Recuperar un usuario por su id
	Usuario recuperar(String id) throws RepositorioException, EntidadNoEncontrada;

	// Recuperar todos los usuarios (devuelve entidades, NO DTOs)
	List<Usuario> recuperarTodos() throws RepositorioException;

	boolean autenticarUsuario(String username, String password) throws RepositorioException, EntidadNoEncontrada;

}
