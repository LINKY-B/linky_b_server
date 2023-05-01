package com.linkyB.backend.common.config.security.token;

import org.hibernate.sql.Delete;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class DeleteAuthenticationToken extends UsernamePasswordAuthenticationToken {

    public DeleteAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }


    public static DeleteAuthenticationToken of(String accessToken, String password){
        return new DeleteAuthenticationToken(accessToken, password);
    }
}
