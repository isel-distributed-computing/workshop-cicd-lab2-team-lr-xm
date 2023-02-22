package todolist.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import todolist.model.CreateToDoListItemRequest;
import todolist.model.ToDoListItem;
import todolist.service.ToDoListService;

@SpringBootTest
@AutoConfigureMockMvc
public class ToDoListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ToDoListService toDoListService;

    @Test
    public void testCreateToDoListItem() throws Exception {
        // Mock input data
        CreateToDoListItemRequest request = new CreateToDoListItemRequest();
        request.setUsername("JohnDoe");
        request.setDescription("Do laundry");

        // Mock ToDoListItem returned by service
        ToDoListItem expectedItem = new ToDoListItem("JohnDoe", "Do laundry");

        // Mock ToDoListService.createToDoListItem() method to return the expected item
        when(toDoListService.createToDoListItem(any(String.class), any(String.class), any(String.class)))
                .thenReturn(expectedItem);

        // Mock authorization header
        String token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";

        // Send request to the controller
        mockMvc.perform(MockMvcRequestBuilders.post("/todolist")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"username\": \"JohnDoe\", \"description\": \"Do laundry\" }"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(expectedItem.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(expectedItem.getDescription()));
    }

}
