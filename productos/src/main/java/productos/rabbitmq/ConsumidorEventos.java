package productos.rabbitmq;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import productos.puertos.ManejadorEventos;
import productos.test.rabbitmq.RabbitMQConfig;

@Component
public class ConsumidorEventos {

	@Autowired
	private ManejadorEventos manejadorEventos;
	
	@RabbitListener(bindings = {
	        @QueueBinding(
	            value = @Queue(value = RabbitMQConfig.QUEUE_NAME, durable = "true"),
	            exchange = @Exchange(value = RabbitMQConfig.EXCHANGE_NAME, type = "topic"),
	            key = "bus.usuarios.#"
	        ),
	        @QueueBinding(
	            value = @Queue(value = RabbitMQConfig.QUEUE_NAME, durable = "true"),
	            exchange = @Exchange(value = RabbitMQConfig.EXCHANGE_NAME, type = "topic"),
	            key = "bus.compraventas.#"
	        )
	})
	public void handleEvent(Message amqpMessage, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {
	    try {
	        // 1. Extraemos el cuerpo del mensaje crudo (Evita el cuelgue por culpa del __TypeId__)
	        String json = new String(amqpMessage.getBody(), StandardCharsets.UTF_8);
	        
	        // 2. Lo convertimos de forma segura a un Map
	        ObjectMapper mapper = new ObjectMapper();
	        Map<String, Object> mensaje = mapper.readValue(json, Map.class);
	        
	        // 3. Redirigimos al manejador según la clave de enrutamiento
	        if ("bus.usuarios.usuario-creado".equals(routingKey)) {
	            this.manejadorEventos.usuarioCreado(
	                    (String) mensaje.get("id"),
	                    (String) mensaje.get("email"),
	                    (String) mensaje.get("nombre"),
	                    (String) mensaje.get("apellidos"));
	        }
	        else if ("bus.compraventas.compraventa-creada".equals(routingKey)) {
	            String idProducto = (String) mensaje.get("idProducto");
	            this.manejadorEventos.compraventaCreada(idProducto);
	        }
	    } catch (Exception e) {
	        System.err.println("Error grave procesando el mensaje de RabbitMQ en Productos: " + e.getMessage());
	    }
	}
}