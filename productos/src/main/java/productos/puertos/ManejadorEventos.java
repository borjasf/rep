package productos.puertos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import modelo.Usuario;
import productos.repositoriosModelo.IRepositorioUsuario;

//Puerto de entrada

@Component
public class ManejadorEventos {
	
	@Autowired
	private IRepositorioUsuario repositorioUsuarios;
	
	public void usuarioCreado(String id, String email, String nombre, String apellidos) {
		Usuario usuario = new Usuario(id, email, nombre, apellidos);
		this.repositorioUsuarios.save(usuario);
	}
	
}