package com.assesment.notification.dataTo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CallBackResponse {
    String eventId;
    String status;
    String eventType;
    String errorMessage;
    Date processedAt;
}
