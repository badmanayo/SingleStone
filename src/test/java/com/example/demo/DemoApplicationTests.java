package com.example.demo;

import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.http.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.*;

import java.io.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = DemoApplication.class)
@AutoConfigureMockMvc
public class DemoApplicationTests {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private AccountRepo repository;

    @Test
    public void getAll() throws Exception{
        String result = "[{\"id\":1,\"name\":{\"first\":\"a\",\"middle\":\"b\",\"last\":\"c\"},\"address\":{\"street\":\"a\",\"city\":\"b\",\"state\":\"c\",\"zip\":\"a\"},\"phone\":[{\"number\":\"asdas\",\"type\":\"home\"}],\"email\":\"b\"},{\"id\":2,\"name\":{\"first\":\"d\",\"middle\":\"e\",\"last\":\"f\"},\"address\":{\"street\":\"a\",\"city\":\"b\",\"state\":\"c\",\"zip\":\"a\"},\"phone\":[{\"number\":\"hmmm\",\"type\":\"brrr\"}],\"email\":\"b\"}]";
        mvc.perform(MockMvcRequestBuilders.get("/contacts")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().bytes(result.getBytes()));
    }

    @Test
    public void getOne() throws Exception {
        String result = "{\"id\":1,\"name\":{\"first\":\"a\",\"middle\":\"b\",\"last\":\"c\"},\"address\":{\"street\":\"a\",\"city\":\"b\",\"state\":\"c\",\"zip\":\"a\"},\"phone\":[{\"number\":\"asdas\",\"type\":\"home\"}],\"email\":\"b\",\"_links\":{\"self\":{\"href\":\"http://localhost/contacts/1\"},\"contacts\":{\"href\":\"http://localhost/contacts\"},\"contacts/call-list\":{\"href\":\"http://localhost/contacts/call-list\"}}}";
        mvc.perform(MockMvcRequestBuilders.get("/contacts/1")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().bytes(result.getBytes()));
    }

    @Test
    public void insertOne() throws Exception {
        String input = "{\"id\": 101,\"name\": {\"first\": \"Harold\",\"middle\": \"Francis\",\"last\": \"Gilkey\"},\"address\": {\"street\": \"8360 High Autumn Row\",\"city\": \"Cannon\",\"state\": \"Delaware\",\"zip\": \"19797\"},\"phone\": [{\"number\": \"302-611-9148\",\"type\": \"home\"},{\"number\": \"302-532-9427\",\"type\": \"mobile\"}],\"email\": \"harold.gilkey@yahoo.com\"}\n";
        String result = "{\"id\":3,\"name\":{\"first\":\"Harold\",\"middle\":\"Francis\",\"last\":\"Gilkey\"},\"address\":{\"street\":\"8360 High Autumn Row\",\"city\":\"Cannon\",\"state\":\"Delaware\",\"zip\":\"19797\"},\"phone\":[{\"number\":\"302-611-9148\",\"type\":\"home\"},{\"number\":\"302-532-9427\",\"type\":\"mobile\"}],\"email\":\"harold.gilkey@yahoo.com\"}";
        mvc.perform(MockMvcRequestBuilders.post("/contacts")
        .contentType(MediaType.APPLICATION_JSON).content(input))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().bytes(result.getBytes()));
    }

    @Test
    public void deleteOne() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/contacts/1")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

        mvc.perform(MockMvcRequestBuilders.get("/contacts/1")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
    }

    @Test
    public void getList() throws Exception{
        String result = "{\"_embedded\":{\"phoneResponseList\":[{\"name\":{\"first\":\"a\",\"middle\":\"b\",\"last\":\"c\"},\"phone\":\"asdas\"}]},\"_links\":{\"contacts\":{\"href\":\"http://localhost/contacts\"}}}";
        mvc.perform(MockMvcRequestBuilders.get("/contacts/call-list")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().bytes(result.getBytes()));
    }
}
