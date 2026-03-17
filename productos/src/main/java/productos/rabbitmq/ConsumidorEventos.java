package productos.test.rabbitmq;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.messaging.handler.annotation.Header;

import productos.puertos.ManejadorEventos;

@Component
public class ConsumidorEventos {

	@Autowired
	private ManejadorEventos manejadorEventos;
	
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void handleEvent(Map<String, Object> mensaje,
			@Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {
	    if (!"bus.usuarios.usuario-creado".equals(routingKey)) {
	    	return;
	    }

	    this.manejadorEventos.usuarioCreado(
	    		(String) mensaje.get("id"),
	    		(String) mensaje.get("email"),
	    		(String) mensaje.get("nombre"),
	    		(String) mensaje.get("apellidos"));
        
    }
}
