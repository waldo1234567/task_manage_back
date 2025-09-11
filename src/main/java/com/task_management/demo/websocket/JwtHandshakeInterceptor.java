package com.task_management.demo.websocket;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

public class JwtHandshakeInterceptor implements HandshakeInterceptor {
    private final JwtDecoder jwtDecoder;

    public JwtHandshakeInterceptor(JwtDecoder jwtDecoder){
        this.jwtDecoder=jwtDecoder;
    }
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        List<String> authHeaders  = request.getHeaders().get("Authorization");
        String token = null;
        if(authHeaders != null && !authHeaders.isEmpty()){
            token = authHeaders.get(0);
        }
        //fallback
        if (token == null) {
            URI uri = request.getURI();
            MultiValueMap<String, String> params = UriComponentsBuilder.fromUri(uri).build().getQueryParams();
            token = params.getFirst("token");
            if (token == null) token = params.getFirst("access_token");
        }

        if(token == null){
            return false;
        }

        if (token.startsWith("Bearer ")) token = token.substring(7);

        try {
            Jwt jwt = jwtDecoder.decode(token);
            attributes.put("jwt", jwt);
            attributes.put("principalName", jwt.getSubject());
            return true;
        }catch (JwtException e){
            return false;
        }
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception
    ){

    }
}
