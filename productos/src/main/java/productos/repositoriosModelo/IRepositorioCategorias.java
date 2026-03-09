package productos.repositoriosModelo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import modelo.Categoria;

@NoRepositoryBean
public interface IRepositorioCategorias extends CrudRepository<Categoria, String> {
	//Definimos los métodos específicos para Categoria si es necesario
	List<Categoria> buscarCategoriasRaiz();
    List<Categoria> buscarDescendientes(String idPadre);

}
