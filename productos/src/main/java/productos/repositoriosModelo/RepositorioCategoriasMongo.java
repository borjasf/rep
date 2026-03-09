package productos.repositoriosModelo;

import org.springframework.data.mongodb.repository.MongoRepository;

import modelo.Categoria;

public interface RepositorioCategoriasMongo extends IRepositorioCategorias, MongoRepository<Categoria, String>{

}
