package ru.rsoi.OAuth2AuthorizationServer.config;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

public class MyJdbcTokenStore extends JdbcTokenStore {

    public MyJdbcTokenStore(DataSource dataSource) {
        super(dataSource);
    }

@Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        OAuth2AccessToken accessToken = null;

        try {
            accessToken = new DefaultOAuth2AccessToken(tokenValue);
        }
        catch (EmptyResultDataAccessException e) {

        }
        catch (IllegalArgumentException e) {

            removeAccessToken(tokenValue);
        }

        return accessToken;
    }
}
