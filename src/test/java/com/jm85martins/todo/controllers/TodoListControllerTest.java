package com.jm85martins.todo.controllers;

import com.jm85martins.todo.TodoApplication;
import com.jm85martins.todo.entities.Item;
import com.jm85martins.todo.entities.TodoList;
import com.jm85martins.todo.repositories.TodoListRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jorgemartins on 03/07/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TodoApplication.class)
@WebAppConfiguration
public class TodoListControllerTest {

    private MediaType contentType = new MediaType("application", "hal+json", Charset.forName("UTF-8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private TodoListRepository todoListRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private String dummyUserId = "123";
    private List<TodoList> todoList = new ArrayList<>();

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        this.todoListRepository.deleteAll();

        this.todoList.add(this.todoListRepository.save(TodoList.builder().name("My first TodoList").userId(dummyUserId).build()));
        this.todoList.add(this.todoListRepository.save(TodoList.builder().name("My second TodoList").userId(dummyUserId).build()));
    }

    @Test
    public void todoListNotFound() throws Exception {
        mockMvc.perform(get("/12345678/todo-list/123456")
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getUserTodoList() throws Exception {
        mockMvc.perform(get("/" + dummyUserId + "/todo-list?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$._embedded.todoListList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.todoListList[0].id", is(this.todoList.get(0).getId())))
                .andExpect(jsonPath("$._embedded.todoListList[0].name", is(this.todoList.get(0).getName())))
                .andExpect(jsonPath("$._embedded.todoListList[1].id", is(this.todoList.get(1).getId())))
                .andExpect(jsonPath("$._embedded.todoListList[1].name", is(this.todoList.get(1).getName())));
    }

    @Test
    public void getSingleTodoList() throws Exception {
        mockMvc.perform(get("/" + dummyUserId + "/todo-list/"
                + this.todoList.get(0).getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is(this.todoList.get(0).getId())))
        ;
    }

    @Test
    public void createNewTodoList() throws Exception {

        List<Item> items = new ArrayList<>();
        items.add(Item.builder().name("Travel to Oporto").build());
        items.add(Item.builder().name("Eat Francesinha").build());

        TodoList todoList = TodoList.builder().name("My awesome todo list for Oporto Travel").build();

        mockMvc.perform(post("/12345678/todo-list")
                .content(this.json(todoList))
                .contentType(contentType))
                .andExpect(status().isOk())
                //.andExpect(redirectedUrlPattern("http://*/12345678/todo-list/*"));
                .andExpect(jsonPath("$.name", is(todoList.getName())));
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
