package br.ufs.dcomp.ChatRabbitMQ;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Emissor {

  private final static String QUEUE_NAME = "chat";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("ip-da-instancia-da-aws"); // Alterar
    factory.setUsername("usuário-do-rabbitmq-server"); // Alterar
    factory.setPassword("senha-do-rabbitmq-server"); // Alterar
    factory.setVirtualHost("/");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    
    String userFrom = System.console().readLine("User: ");
    String userTo = '';
    Boolean exit = false;

    while(!exit) {
      String message = System.console().readLine(userTo + ">> ");
      if(message == "!exit") {
        exit = true;
        continue;
      }
      if(userTo.startsWith('@')) {
        
        userTo = message;
      } else {
        String messageBody = userTo + ';' + message;
        channel.queueDeclare(userTo, false, false, false, null);
        channel.basicPublish("", userTo, null, messageBody.getBytes("UTF-8"));
      }   
    }
    
    channel.close();
    connection.close();
  }
}