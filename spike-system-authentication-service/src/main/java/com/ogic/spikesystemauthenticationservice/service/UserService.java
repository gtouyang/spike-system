package com.ogic.spikesystemauthenticationservice.service;


import java.util.Optional;

/**
 * @author ogic
 */
public interface UserService {

    String login(String username, String password);

    Optional findByToken(String token);
}
