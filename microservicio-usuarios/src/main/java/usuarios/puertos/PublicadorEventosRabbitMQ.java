package usuarios.puertos;

import java.io.IOException;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import usuarios.eventos.Evento;

public class PublicadorEventosRabbitMQ implements PublicadorEventos {
	
	public PublicadorEventosRabbitMQ() {
		
		//declarar el bus de eventos. 
		try{
			String uri = "amqp://guest:guest@localhost:5672";
			
			ConnectionFactory factory = new ConnectionFactory();
			factory.setUri(uri);
	
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			//tenemos que meter nuestro objeto evento en un mensaje, para eso lo convertimos a json
			String exchangeName = "bus";
			boolean durable = true;
			channel.exchangeDeclare(exchangeName, "topic", durable);
			channel.close();
			connection.close();
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void publicarEvento(Evento evento) throws IOException {
		try{
			String uri = "amqp://guest:guest@localhost:5672";
			
			ConnectionFactory factory = new ConnectionFactory();
			factory.setUri(uri);
	
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			//tenemos que meter nuestro objeto evento en un mensaje, para eso lo convertimos a json
			Gson gson = new Gson();
			
			String mensaje = gson.toJson(evento);
	
			channel.basicPublish("bus", "bus.usuarios." + evento.getTipoEvento(), new AMQP.BasicProperties.Builder()
					.contentType("application/json")
					.build(), mensaje.getBytes());
			
			channel.close();
			connection.close();
		}catch(Exception e) {
			throw new IOException(e);
		}
	}

}
