package todolist.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import todolist.model.CreateToDoListItemRequest;
import todolist.model.ToDoListItem;
import todolist.service.ToDoListService;
import todolist.service.UnauthorizedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/todolist")
@Tag(name = "ToDoListController", description = "Operations to manage todo list items")
public class ToDoListController {
    private static final Logger logger = LoggerFactory.getLogger(ToDoListController.class);
    public static final String BEARER = "Bearer ";
    private final ToDoListService toDoListService;
    @Autowired
    public ToDoListController(ToDoListService toDoListService) {
        this.toDoListService = toDoListService;
    }

    @PostMapping()
    public ToDoListItem createToDoListItem(@RequestBody CreateToDoListItemRequest request,
                                           @RequestHeader("Authorization") String authorization)
            throws UnauthorizedException, IOException, TimeoutException
    {
        logger.info("Create todo list item");
        String token = getTokenFromAuthorizationHeader(authorization);
        ToDoListItem item = toDoListService.createToDoListItem(token, request.getUsername(), request.getDescription());
        return item;
    }

    private String getTokenFromAuthorizationHeader(String authorization) {
        if (!authorization.startsWith(BEARER))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid JWT token");
        return authorization.substring(BEARER.length());
    }

    @DeleteMapping("/{itemId}")
    public ToDoListItem deleteToDoListItem(@PathVariable("itemId") int itemId,
                                           @RequestHeader("Authorization") String authorization)
            throws IOException, TimeoutException, UnauthorizedException {
        logger.info("Delete todo list item");
        String token = getTokenFromAuthorizationHeader(authorization);
        Optional<ToDoListItem> item = toDoListService.deleteToDoListItem(token, itemId);
        if (!item.isEmpty()) {
            return item.get();
        }
        throw new ResourceNotFoundException();
    }

    @GetMapping("/{username}")
    public List<ToDoListItem> getAllItemsByUser(@PathVariable("username") String username,
                                                @RequestHeader("Authorization") String authorization) throws UnauthorizedException {
        logger.info("Get all items from user");
        String token = getTokenFromAuthorizationHeader(authorization);
        Optional<List<ToDoListItem>> list = toDoListService.getToDoListItemList(token, username);
        if (!list.isEmpty())
            return list.get();
        return new ArrayList<>();
    }
}

