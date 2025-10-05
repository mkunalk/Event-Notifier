package com.assesment.notification.controller;

import com.assesment.notification.dataTo.EventRequest;
import com.assesment.notification.dataTo.EventResponse;
import com.assesment.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@RestController
@RequiredArgsConstructor
@EnableWebMvc
public class NotificationController {

    private final NotificationService notificationService;
    @PostMapping(value = "/api/events", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventResponse> eventHandler(@RequestBody EventRequest request){

        EventResponse response = notificationService.processEvents(request);
        return ResponseEntity.ok().body(response);


    }
}
