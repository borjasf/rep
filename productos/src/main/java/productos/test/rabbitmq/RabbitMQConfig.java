package productos.test.rabbitmq;

import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	public static final String EXCHANGE_NAME = "bus";
	public static final String QUEUE_NAME = "productos";
	public static final String ROUTING_KEY = "bus.usuarios.#";
	public static final String BINDING_KEY = "bus.usuarios.prueba"; 

	// ¡ATENCIÓN! Hemos borrado el @Bean public MessageConverter jsonMessageConverter()
	// Ahora Spring dejará pasar el mensaje crudo sin intentar adivinar su clase.
}