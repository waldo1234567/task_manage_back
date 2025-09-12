package com.task_management.demo.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PresenceService {
    private final StringRedisTemplate redis;
    private final SimpMessagingTemplate messagingTemplate;

    public PresenceService(StringRedisTemplate redis, SimpMessagingTemplate messagingTemplate) {
        this.redis = redis;
        this.messagingTemplate = messagingTemplate;
    }

    //redis helpers
    private String projectKey(String projectId){return "presence:project:" + projectId;}
    private String sessionKey(String sessionId) { return "presence:session:" + sessionId; }
    private String subscriptionKey(String sessionId, String subscriptionId) { return "presence:subscription:" + sessionId + ":" + subscriptionId; }

    public void addPresence(String projectId, String sessionId, String subscriptionId, String auth0Id, String displayName){
        if(projectId == null || auth0Id == null ) return;
        String memberValue = auth0Id + "|" + (displayName == null ? "" : displayName);
        redis.opsForSet().add(projectKey(projectId), memberValue);
        redis.opsForSet().add(sessionKey(sessionId), projectId);
        redis.opsForValue().set(subscriptionKey(sessionId, subscriptionId), projectId);
        broadcastPresence(projectId);
    }

    public void removeSubscription(String sessionId, String subscriptionId, String auth0Id){
        String subKey = subscriptionKey(sessionId, subscriptionId);
        String projectId = redis.opsForValue().get(subKey);
        if(projectId != null){
            redis.delete(subKey);
            removeMemberFromProject(projectId, auth0Id);
            redis.opsForSet().remove(sessionKey(sessionId), projectId);
            broadcastPresence(projectId);
        }
    }

    public void removeSession(String sessionId, String auth0Id){
        Set<String> projectIds = redis.opsForSet().members(sessionKey(sessionId));
        if (projectIds != null) {
            for (String projectId : projectIds) {
                removeMemberFromProject(projectId, auth0Id);
                broadcastPresence(projectId);
            }
        }
        redis.delete(sessionKey(sessionId));
    }

    private void removeMemberFromProject(String projectId, String auth0Id){
        if (auth0Id == null) return;
        Set<String> members = redis.opsForSet().members(projectKey(projectId));
        if (members == null) return;
        List<String> toRemove = members.stream()
                .filter(m -> m.startsWith(auth0Id + "|"))
                .collect(Collectors.toList());
        if (!toRemove.isEmpty()) {
            redis.opsForSet().remove(projectKey(projectId), toRemove.toArray());
        }
    }

    public List<Map<String,String>> listPresence(String projectId){
        Set<String> members = redis.opsForSet().members(projectKey(projectId));
        if (members == null) return Collections.emptyList();
        List<Map<String, String>> out = new ArrayList<>();
        for (String m : members) {
            String[] parts = m.split("\\|", 2);
            Map<String,String> map = new HashMap<>();
            map.put("auth0Id", parts[0]);
            map.put("displayName", parts.length > 1 ? parts[1] : "");
            out.add(map);
        }
        return out;
    }

    public void broadcastPresence(String projectId) {
        String topic = "/topic/project." + projectId + ".presence";
        List<Map<String, String>> presences = listPresence(projectId);
        var payload = new HashMap<String, Object>();
        payload.put("type", "presence.update");
        payload.put("projectId", projectId);
        payload.put("members", presences);
        messagingTemplate.convertAndSend(topic, payload);
    }
}
