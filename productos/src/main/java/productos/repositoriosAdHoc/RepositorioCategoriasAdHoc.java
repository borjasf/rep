package productos.repositoriosAdHoc;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import modelo.Categoria;
import productos.repositorio.EntidadNoEncontrada;
import productos.repositorio.RepositorioException;

@Repository
public interface RepositorioCategoriasAdHoc  extends JpaRepository<Categoria, String> {
	
	@Query("SELECT c FROM Categoria c WHERE c NOT IN "
	         + "(SELECT sub FROM Categoria parent JOIN parent.subcategorias sub)")
	List<Categoria> buscarCategoriasRaiz() throws RepositorioException, EntidadNoEncontrada; 
	
	@Query("SELECT sub FROM Categoria parent JOIN parent.subcategorias sub WHERE parent.id = :idPadre")
	List<Categoria> buscarDescendientes(@Param("idCategoriaPadre") String idCategoriaPadre) throws RepositorioException, EntidadNoEncontrada;
}
