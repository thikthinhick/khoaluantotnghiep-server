package com.vnu.server.config;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.lang.reflect.Field;
public class ServletAwareConfigurator extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
        HttpServletRequest httpservletRequest = getField(request, HttpServletRequest.class);
        String accessToken = httpservletRequest.getHeader("access_token");
        config.getUserProperties().put("access_token", accessToken);
    }
    private static < I, F > F getField(I instance, Class < F > fieldType) {
        try {
            for (Class < ? > type = instance.getClass(); type != Object.class; type = type.getSuperclass()) {
                for (Field field: type.getDeclaredFields()) {
                    if (fieldType.isAssignableFrom(field.getType())) {
                        field.setAccessible(true);
                        return (F) field.get(instance);
                    }
                }
            }
        } catch (Exception e) {

        }
        return null;
    }
}