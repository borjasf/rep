package productos.repositoriosModelo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import modelo.Producto;

public interface RepositorioProductoMongo extends IRepositorioProducto, MongoRepository<Producto, String> {
	
	@Override
    @Query("{ 'mes_publicacion' : ?0, 'ano_publicacion' : ?1 }") 
    Page<Producto> findProductosByMonthAndYear(int mes, int ano, Pageable pageable);
	
}
