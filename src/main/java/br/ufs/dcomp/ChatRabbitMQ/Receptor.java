package br.ufs.dcomp.ChatRabbitMQ;
import com.rabbitmq.client.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.until.Date;

public class Recptor {

  private final static String QUEUE_NAME = "chat";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("ip-da-instancia-da-aws"); // Alterar
    factory.setUsername("usuário-do-rabbitmq-server"); // Alterar
    factory.setPassword("senha-do-rabbitmq-server"); // Alterar
    factory.setVirtualHost("/");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    
                      //(queue-name, durable, exclusive, auto-delete, params); 
    channel.queueDeclare(QUEUE_NAME, false,   false,     false,       null);
    
    Consumer consumer = new DefaultConsumer(channel) {
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)           throws IOException {
        String messageBody = new String(body, "UTF-8");
        
        String message = this.getformatedDate(messageBody)
        String date = this.getformatedDate();        

        System.out.println(date + ' ' + message);
      }
    };
                      //(queue-name, autoAck, consumer);    
    channel.basicConsume(QUEUE_NAME, true,    consumer);
    
  }

  private String formatMessage (String messageBody) {
    int breakPoint = messageBody.indexOf(';');
    
    if(breakPoint == -1) throws new Error();
    
    String userFrom = messageBody.subString(0, breakPoint).replace("@", "");
    String message = messageBody.subString(breakPoint + 1);

    return userFrom + ' diz: ' + message; 
  }

  private String getformatedDate() {
    Date currentDate = new Date();
    SimpleDateFormat dateFormater = new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm");
    return dateFormater.format(currentDate);
  }
}