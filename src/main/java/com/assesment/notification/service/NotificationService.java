package com.assesment.notification.service;

import com.assesment.notification.dataTo.EventRequest;
import com.assesment.notification.dataTo.EventResponse;

public interface NotificationService {

    EventResponse processEvents(EventRequest request);
}
