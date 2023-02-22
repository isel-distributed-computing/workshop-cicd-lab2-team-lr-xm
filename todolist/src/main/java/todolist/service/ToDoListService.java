package todolist.service;

// ToDoListService.java

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import todolist.controller.ResponseDetails;
import todolist.model.ToDo;
import todolist.model.ToDoListItem;
import todolist.repository.ToDoRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

@Service
public class ToDoListService {
    private static final Logger logger = LoggerFactory.getLogger(ToDoListService.class);
    private ConnectionFactory mqFactory;

    @Value("${todolist.QUEUE_NAME}")
    private String mqQueueName;
    @Value("${todolist.RABBITMQ_HOST}")
    private String mqHostName;
    @Value("${todolist.RABBITMQ_USER}")
    private String mqUserName;
    @Value("${todolist.RABBITMQ_PASS")
    private String mqPassword;

    @Value("${todolist.AUTH_SERVICE_URL}")
    private String authServiceURL;

    @Autowired
    private ToDoRepository toDoListRepository;

    public ToDoListItem createToDoListItem(String authorization, String username, String description) throws UnauthorizedException, IOException, TimeoutException {
        // Validate authorization
        if (!validateTokenWithAuthService(authorization)) throw new UnauthorizedException();

        // Create a new to-do list item
        ToDoListItem item = new ToDoListItem(username, description);
        // Save the item to the database
        ToDo todo = saveToDoListItem(item);

        // Send a notification
        sendNotification(item, Action.CREATE);
        return item;
    }

    public Optional<ToDoListItem> deleteToDoListItem(String authorization, long itemId)
            throws IOException, TimeoutException, UnauthorizedException
    {
        logger.info("ToDo item delete");
        // Validate authorization token
        if (!validateTokenWithAuthService(authorization)) throw new UnauthorizedException();

        // Get the item from the database
        ToDo item = toDoListRepository.getReferenceById(itemId);
        if (item == null) return Optional.empty();
        ToDoListItem toDoListItem = new ToDoListItem(item.getUsername(), item.getDescription());
        toDoListRepository.deleteById(itemId);

        // Send a notification
        sendNotification(toDoListItem, Action.DELETE);
        return Optional.ofNullable(toDoListItem);
    }

    public Optional<List<ToDoListItem>> getToDoListItemList(String authorization, String username)
            throws UnauthorizedException
    {
        logger.info("Get ToDo list of all item by user");
        // Validate authorization token
        if (!validateTokenWithAuthService(authorization)) throw new UnauthorizedException();

        // Get all items from the database
        List<ToDoListItem> allItems = new ArrayList<>();
        toDoListRepository.findAllByUsername(username).forEach(item -> {
            allItems.add(new ToDoListItem(item.getUsername(), item.getDescription()));
        });
        return Optional.ofNullable(allItems);
    }

    private ToDo saveToDoListItem(ToDoListItem item)  {
        logger.info("Save ToDo item");
        ToDo toDo = new ToDo(item.getUsername(), item.getDescription());
        return toDoListRepository.save(toDo);
    }

    private void sendNotification(ToDoListItem item, Action create) throws IOException, TimeoutException {
        if (mqFactory == null) {
            initFactory();
        }
        logger.info("Sending notification for "
                + item.getUsername()
                + " with action "
                + create.toString()
                + " to queue " + mqQueueName);
        // Create a connection and channel
        Connection connection = mqFactory.newConnection();
        Channel channel = connection.createChannel();

        // Publish a message to the queue
        //    - crate a JSON object
        //    - convert the JSON object to a string
        //    - publish the string to the queue

        // Create an ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();
        // Create a new ObjectNode to represent the JSON object
        ObjectNode jsonObject = objectMapper.createObjectNode();
        // Add properties to the JSON object
        jsonObject.put("username", item.getUsername());
        jsonObject.put("action", create.toString());
        // Convert the JSON object to a string
        String jsonString = jsonObject.toString();
        channel.basicPublish("", mqQueueName, null, jsonString.getBytes());
        // Close the channel and connection
        channel.close();
        connection.close();
    }

    private void initFactory() {
        // Create a factory connection and set the connection parameters
        mqFactory = new ConnectionFactory();
        mqFactory.setHost(mqHostName);
        //mqFactory.setUsername(mqUserName);
        //mqFactory.setPassword(mqPassword);
    }

    private boolean validateTokenWithAuthService(String authorization) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);

        /*String body = authorization;
        byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);
        HttpEntity<byte[]> request = new HttpEntity<>(bodyBytes, headers);*/

        HttpEntity<String> request = new HttpEntity<>(authorization, headers);

        String authEndpoint = authServiceURL + "/user/validate";
        logger.info("Calling auth service at " + authEndpoint);
        ResponseEntity<ResponseDetails> response = restTemplate.postForEntity(authEndpoint, request, ResponseDetails.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            ResponseDetails responseDetails = response.getBody();
            return responseDetails.getCode().equals("Ok");
        } else {
            return false;
        }
    }
}

