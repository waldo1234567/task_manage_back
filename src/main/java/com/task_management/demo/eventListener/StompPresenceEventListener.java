package com.task_management.demo.eventListener;

import com.task_management.demo.service.PresenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Component
public class StompPresenceEventListener {
    private static final Logger log = LoggerFactory.getLogger(StompPresenceEventListener.class);
    private final PresenceService presenceService;

    public StompPresenceEventListener(PresenceService presenceService) {
        this.presenceService = presenceService;
    }

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = accessor.getDestination();
        String sessionId = accessor.getSessionId();
        var user = accessor.getUser();
        String principalName = user != null ? user.getName() : null;
        String subscriptionId = accessor.getHeader("simpSubscriptionId").toString();

        if(destination != null && destination.startsWith("/topic/project.")){
            String projectId = extractProjectId(destination);
            if(projectId != null && principalName != null){
                presenceService.addPresence(projectId, sessionId, subscriptionId, principalName, "");
                log.debug("subscribe -> project {} session {} user {}", projectId, sessionId, principalName);
            }
        }
    }

    @EventListener
    public void handleSessionUnsubscribeEvent(SessionUnsubscribeEvent event){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        String subscriptionId = accessor.getHeader("simpSubscriptionId").toString();
        var user = accessor.getUser();
        String principalName = user != null ? user.getName() : null;

        presenceService.removeSubscription(sessionId, subscriptionId, principalName);
        log.debug("unsubscribe -> session {} sub {}", sessionId, subscriptionId);
    }

    @EventListener
    public void handleSessionDisconnectEvent(SessionDisconnectEvent event){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        var user = accessor.getUser();
        String principalName = user != null ? user.getName() : null;

        presenceService.removeSession(sessionId, principalName);
        log.debug("disconnect -> session {} user {}", sessionId, principalName);
    }

    //helper
    private String extractProjectId(String destination){
        int p = destination.indexOf("/topic/project.");
        if (p == -1) return null;
        String tail = destination.substring(p + "/topic/project.".length());
        int dot = tail.indexOf('.');
        if (dot != -1) {
            return tail.substring(0, dot);
        }
        return tail;
    }


}
