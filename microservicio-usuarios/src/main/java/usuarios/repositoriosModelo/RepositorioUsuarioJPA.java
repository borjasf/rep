package usuarios.repositoriosModelo;

import usuarios.modelo.Usuario;
import usuarios.repositorio.RepositorioJPA;

public class RepositorioUsuarioJPA  extends RepositorioJPA<Usuario> implements IRepositorioUsuario{

	@Override
	public Class<Usuario> getClase() {
		return Usuario.class;
	}
	
	

}
