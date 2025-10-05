package com.assesment.notification.controllerTest;

import com.assesment.notification.controller.NotificationController;
import com.assesment.notification.dataTo.EventRequest;
import com.assesment.notification.dataTo.EventResponse;
import com.assesment.notification.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(classes = {NotificationController.class})
@AutoConfigureMockMvc
public class NotificationControllerTest {

    @MockitoBean
    private NotificationService notificationService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test()
    @DisplayName("eventHandlerPass")
    public void eventHandlerTest() throws Exception {
        EventRequest request = new EventRequest();
        request.setEventType("EMAIL");
        request.setCallbackUrl("https");
        request.setRecipient("abc@gmial.com");
        request.setMessage("hello");
        String res = mapper.writeValueAsString(request);
        Mockito.when(notificationService.processEvents(Mockito.any())).thenReturn(new EventResponse("1","fs"));

        RequestBuilder builder =  MockMvcRequestBuilders.post("/api/events").contentType(MediaType.APPLICATION_JSON).content(res);

        MvcResult result = mockMvc.perform(post("/api/events").contentType(MediaType.APPLICATION_JSON_VALUE).content(res)).andReturn();
        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(),response.getStatus());
        assertThat(response.getContentAsString()).isNotNull();

        verify(notificationService,times(1)).processEvents(Mockito.any());

    }

    @Test()
    @DisplayName("eventHandlerFail")
    public void eventHandlerTest2() throws Exception {
        EventRequest request = new EventRequest();
        request.setCallbackUrl("https");
        request.setRecipient("abc@gmial.com");
        request.setMessage("hello");
        String res = mapper.writeValueAsString(request);
        Mockito.when(notificationService.processEvents(Mockito.any())).thenReturn(new EventResponse("1","fs"));

        RequestBuilder builder =  MockMvcRequestBuilders.post("/api/events").contentType(MediaType.APPLICATION_JSON).content(res);

        MvcResult result = mockMvc.perform(post("/api/events").contentType(MediaType.APPLICATION_JSON_VALUE).content(res)).andReturn();
        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatus());
        assertThat(response.getContentAsString()).isNotNull();

    }
}
