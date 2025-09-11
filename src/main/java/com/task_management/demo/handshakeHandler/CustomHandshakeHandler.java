package com.task_management.demo.handshakeHandler;

import com.task_management.demo.Principal.StompPrincipal;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

public class CustomHandshakeHandler extends DefaultHandshakeHandler {
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler webSocketHandler, Map<String,Object> attributes){
        Object name = attributes.get("principalName");
        if(name instanceof  String){
            return new StompPrincipal((String) name);
        }
        return super.determineUser(request, webSocketHandler, attributes);
    }
}
