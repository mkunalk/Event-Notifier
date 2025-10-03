package com.assesment.notification.controller;

import com.assesment.notification.dataTo.EventRequest;
import com.assesment.notification.dataTo.EventResponse;
import com.assesment.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    @PostMapping("/api/events")
    public ResponseEntity<EventResponse> eventHandler(@RequestBody EventRequest request){

        EventResponse response = notificationService.processEvents(request);
        return ResponseEntity.ok().body(response);


    }
}
