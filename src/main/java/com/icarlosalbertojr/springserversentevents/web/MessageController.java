package com.icarlosalbertojr.springserversentevents.web;

import com.icarlosalbertojr.springserversentevents.event.EventName;
import com.icarlosalbertojr.springserversentevents.event.EventSource;
import com.icarlosalbertojr.springserversentevents.event.SseEmitterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@CrossOrigin("*")
@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

    private final SseEmitterService sseEmitterService;

    @GetMapping("/emitter")
    public SseEmitter registerNewEmitter() {
        return sseEmitterService.newEmitter();
    }

    @PostMapping("/send")
    public void sendMessage(@RequestBody MessageEventInput message) {

        var source = EventSource.builder()
                .emitterId(message.getEid())
                .eventName(EventName.MESSAGE_EVENT)
                .source(message.getMessage())
                .build();

        sseEmitterService.sendEvent(source);
    }
}





