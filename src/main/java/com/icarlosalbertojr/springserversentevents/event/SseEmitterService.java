package com.icarlosalbertojr.springserversentevents.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
@Service
public class SseEmitterService {

    private Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    public SseEmitter newEmitter() {
        var emitter = new SseEmitter(Long.MAX_VALUE);

        var emitterId = UUID.randomUUID().toString();
        sseEmitters.put(emitterId, emitter);

        var eventName = EventName.NEW_EMITTER_EVENT;
        var source = EventSource.builder()
                .emitterId(emitterId)
                .eventName(eventName)
                .build();

        emitter.onCompletion(() -> sseEmitters.remove(emitterId));
        emitter.onTimeout(() -> sseEmitters.remove(emitterId));

        sendEvent(source);
        return emitter;
    }

    public void sendEvent(EventSource source) {
        try {
            var mapper = new ObjectMapper();

            var emitter = sseEmitters.get(source.getEmitterId());
            var event = SseEmitter.event()
                    .name(source.getEventName())
                    .data(mapper.writeValueAsString(source));

            emitter.send(event);
        } catch (Exception e) {
            throw new RuntimeException("Send event error", e);
        }
    }

}
