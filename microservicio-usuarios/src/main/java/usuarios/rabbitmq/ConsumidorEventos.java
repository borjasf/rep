package usuarios.rabbitmq;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import usuarios.eventos.EventoCompraventaCreada;
import usuarios.puertos.ManejadorEventos;

public class ConsumidorEventos {

	private static final String DEFAULT_RABBITMQ_URI = "amqp://guest:guest@localhost:5672";
	private static final String EXCHANGE_NAME = "bus";
	private static final String QUEUE_NAME = "usuarios";
	private static final String BINDING_KEY = "bus.compraventas.#";
	private static final String COMPRAVENTA_CREADA_KEY = "bus.compraventas.compraventa-creada";

	private final ManejadorEventos manejador = new ManejadorEventos();
	private final Gson gson = new Gson();
	private Connection connection;
	private Channel channel;

	public void start() {
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setUri(getRabbitMqUri());

			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.exchangeDeclare(EXCHANGE_NAME, "topic", true);
			channel.queueDeclare(QUEUE_NAME, true, false, false, null);
			channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, BINDING_KEY);

			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
				long deliveryTag = delivery.getEnvelope().getDeliveryTag();
				String routingKey = delivery.getEnvelope().getRoutingKey();

				try {
					if (COMPRAVENTA_CREADA_KEY.equals(routingKey)) {
						String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
						EventoCompraventaCreada evento = gson.fromJson(message, EventoCompraventaCreada.class);

						manejador.procesarEventoCompraventaCreada(
							evento.getEmailComprador(),
							evento.getEmailVendedor());
					}

					channel.basicAck(deliveryTag, false);
				} catch (Exception e) {
					channel.basicNack(deliveryTag, false, true);
				}
			};

			channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {
			});
		} catch (Exception e) {
			throw new RuntimeException("No se ha podido iniciar el consumidor RabbitMQ de usuarios", e);
		}
	}

	public void end() {
		try {
			if (channel != null) {
				channel.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (IOException | TimeoutException e) {
			throw new RuntimeException("No se ha podido detener el consumidor RabbitMQ de usuarios", e);
		}
	}

	private String getRabbitMqUri() {
		String rabbitMqUri = System.getenv("RABBITMQ_URI");
		return rabbitMqUri == null || rabbitMqUri.trim().isEmpty() ? DEFAULT_RABBITMQ_URI : rabbitMqUri;
	}
}