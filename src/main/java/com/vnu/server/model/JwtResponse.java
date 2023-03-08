package com.vnu.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String username;
    private String fullName;
    private Long userId;
    private List<String> roles;
}
