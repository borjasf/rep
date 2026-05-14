package usuarios.utils;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class EntityManagerHelper {
    private static EntityManagerFactory entityManagerFactory;
    private static final ThreadLocal<EntityManager> entityManagerHolder;
    private static Map<String, String> overridesLeidos = new HashMap<>();

    static {    
        overridesLeidos = cargarOverrides();
        entityManagerFactory = Persistence.createEntityManagerFactory("usuariosUP", overridesLeidos);
        entityManagerHolder = new ThreadLocal<EntityManager>();
    }

    private static Map<String, String> cargarOverrides() {
      Map<String, String> overrides = new HashMap<>();
      putIfPresent(overrides, "javax.persistence.jdbc.url", System.getenv("JDBC_URL"));
      putIfPresent(overrides, "javax.persistence.jdbc.user", System.getenv("JDBC_USERNAME"));
      putIfPresent(overrides, "javax.persistence.jdbc.password", System.getenv("JDBC_PASSWORD"));
      return overrides;
    }

    private static void putIfPresent(Map<String, String> map, String key, String value) {
      if (value != null && !value.trim().isEmpty()) {
          map.put(key, value);
      }
  }

    public static EntityManager getEntityManager() {
        EntityManager entityManager = entityManagerHolder.get();
        if (entityManager == null || !entityManager.isOpen()) {
            entityManager = entityManagerFactory.createEntityManager();
            entityManagerHolder.set(entityManager);
        }
        return entityManager;
    }
    public static void closeEntityManager() {
        EntityManager entityManager = entityManagerHolder.get();
        if (entityManager != null) {
            entityManagerHolder.set(null);
            entityManager.close();
        }
    }
}