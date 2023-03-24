package com.icarlosalbertojr.springserversentevents.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventSource {

    private String emitterId;
    private EventName eventName;
    private Object source;

    public String getEventName() {
        return eventName.name();
    }
}
