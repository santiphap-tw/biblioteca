package com.twu.biblioteca.integration.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twu.biblioteca.App;
import com.twu.biblioteca.Biblioteca;
import com.twu.biblioteca.model.Label;
import com.twu.biblioteca.model.Rental;
import com.twu.biblioteca.model.RestResponse;
import com.twu.biblioteca.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes= App.class)
@RunWith(SpringRunner.class)
public class CheckOutControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    private JacksonTester<RestResponse> itemJson;
    private Rental item;

    @Before
    public void setup() {
        // Given
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        App.biblioteca = new Biblioteca();
        ObjectMapper objectMapper = new ObjectMapper();
        JacksonTester.initFields(this, objectMapper);
        // Login with some user
        User user = App.biblioteca.user().getUsers().get(0);
        App.biblioteca.user().login(user.getId(),user.getPassword());
        // Get some item
        item = App.biblioteca.getItems(Biblioteca.FILTER.AVAILABLE).get(0);
    }

    @Test
    public void shouldCheckOutCorrectItem() throws Exception  {
        // Given
        RestResponse expectedResult = new RestResponse(RestResponse.STATUS.SUCCESS, Label.CHECKOUT_SUCCESS.text);
        String json = itemJson.write(expectedResult).getJson();
        // When
        this.mockMvc.perform(get("/checkout/" + item.getTitle()))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    public void shouldNotCheckOutNAItem() throws Exception  {
        // Given
        App.biblioteca.doCheckOut(item.getTitle());
        RestResponse expectedResult = new RestResponse(RestResponse.STATUS.FAIL, Label.CHECKOUT_FAIL.text);
        String json = itemJson.write(expectedResult).getJson();
        // When
        this.mockMvc.perform(get("/checkout/" + item.getTitle()))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    public void shouldNotCheckOutWrongItem() throws Exception  {
        // Given
        RestResponse expectedResult = new RestResponse(RestResponse.STATUS.FAIL, Label.CHECKOUT_FAIL.text);
        String json = itemJson.write(expectedResult).getJson();
        // When
        this.mockMvc.perform(get("/checkout/there_is_no_this_item_name"))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    public void shouldNotCheckOutWhenNotLogin() throws Exception  {
        // Given
        App.biblioteca.user().logout();
        RestResponse expectedResult = new RestResponse(RestResponse.STATUS.FAIL, Label.AUTHORIZATION_ERROR.text);
        String json = itemJson.write(expectedResult).getJson();
        // When
        this.mockMvc.perform(get("/checkout/" + item.getTitle()))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }
}