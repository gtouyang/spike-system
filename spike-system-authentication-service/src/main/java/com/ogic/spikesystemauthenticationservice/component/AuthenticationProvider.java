package com.ogic.spikesystemauthenticationservice.component;

import com.ogic.spikesystemauthenticationservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Optional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
public class AuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    UserService userService;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        //
    }

    @Override
    protected UserDetails retrieveUser(String userName, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

        Object token = usernamePasswordAuthenticationToken.getCredentials();
        UserDetails userDetails = null;
        try {
            userDetails = (UserDetails) Optional
                    .ofNullable(token)
                    .map(String::valueOf)
                    .flatMap(userService::findByToken)
                    .orElseThrow(() -> new UsernameNotFoundException("Cannot find user with authentication token=" + token));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return userDetails;
    }
}