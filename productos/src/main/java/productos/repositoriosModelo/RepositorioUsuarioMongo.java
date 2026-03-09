package productos.repositoriosModelo;

import org.springframework.data.mongodb.repository.MongoRepository;

import modelo.Usuario;

public interface RepositorioUsuarioMongo extends IRepositorioUsuario, MongoRepository<Usuario, String> {

}
