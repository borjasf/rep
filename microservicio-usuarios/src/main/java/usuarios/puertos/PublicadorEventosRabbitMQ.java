package usuarios.puertos;

import java.io.IOException;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import usuarios.eventos.Evento;

public class PublicadorEventosRabbitMQ implements PublicadorEventos {
	
	private final String uri;
	
	public PublicadorEventosRabbitMQ() {
		// Leemos la variable de entorno de Docker. Si no existe, usamos la de la nube por defecto.
		String envUri = System.getenv("RABBITMQ_URI");
		this.uri = (envUri != null && !envUri.isEmpty()) ? envUri : "amqps://oklxofze:TjosECA3Q1TT7Y4XbVEnj7XBnXym52qD@rat.rmq2.cloudamqp.com/oklxofze";
		
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setUri(this.uri);
	
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			String exchangeName = "bus";
			boolean durable = true;
			channel.exchangeDeclare(exchangeName, "topic", durable);
			channel.close();
			connection.close();
		} catch(Exception e) {
			throw new RuntimeException("Error al conectar con RabbitMQ: " + e.getMessage(), e);
		}
	}
	
	@Override
	public void publicarEvento(Evento evento) throws IOException {
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setUri(this.uri);
	
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			
			Gson gson = new Gson();
			String mensaje = gson.toJson(evento);
	
			// La routing key será algo como "bus.usuarios.usuario-creado"
			channel.basicPublish("bus", "bus.usuarios." + evento.getTipoEvento(), new AMQP.BasicProperties.Builder()
					.contentType("application/json")
					.build(), mensaje.getBytes());
			
			System.out.println(" [x] Enviado a RabbitMQ: '" + mensaje + "'");
			
			channel.close();
			connection.close();
		} catch(Exception e) {
			throw new IOException("Error publicando en RabbitMQ: " + e.getMessage(), e);
		}
	}
}