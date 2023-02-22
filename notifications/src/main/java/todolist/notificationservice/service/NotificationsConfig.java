package todolist.notificationservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import todolist.notificationservice.model.EventModel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

@Configuration
public class NotificationsConfig {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Value("${spring.rabbitmq.host}")
    private String HOST;
    /*
      There could be many ways to disseminate the event to consumers,
      using SMS, E-MAIL, queues, or  PUB/SUB solutions.
      This simple implementation uses the console
       */
    @Bean
    public INotificationStrategy getSubscriber1() {
        return new INotificationStrategy() {
            @Override
            public void sendNotification(EventModel evt) {
                logger.info(String.format("Subscriber1: '%s'",evt));
            }
        };

    }

    @Bean
    public INotificationStrategy getSubscriber2() {
        return new INotificationStrategy() {
            @Override
            public void sendNotification(EventModel evt) {
                logger.info(String.format("Subscriber2: RabbitMQ",evt));

                String QUEUE_NAME="notification_response";
                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost(HOST);
                try (Connection connection = factory.newConnection();
                    Channel channel = connection.createChannel()) {

                    ObjectMapper mapper = new ObjectMapper();

                    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                    String message = mapper.writeValueAsString(evt);
                    channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                }
                catch (Exception ex)
                {
                    logger.error("Error sending notification: ",ex);
                }
            }
        };

    }
}
