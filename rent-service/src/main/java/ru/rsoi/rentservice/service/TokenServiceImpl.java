package ru.rsoi.rentservice.service;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    private String TOKEN = UUID.randomUUID().toString();
    private final String APPID = "8d03e8dc-5abc-4652-92f0-596a979f46d0";
    private final String APPSECRET = "80190b1a-1ebe-453f-9d1e-5e43afcf6ea2";

    public TokenServiceImpl() {
        Thread timeoutThread = new Thread(this::timeout,"timeoutThread");
        timeoutThread.start();
    }

    @Override
    public String login(String username, String password) {
        if(username.equals(APPID) && password.equals(APPSECRET)){
            setTOKEN();
            return TOKEN;
        }
        return "";
    }

    @Override
    public Optional<User> findByToken(String token) {
        if(TOKEN.equals(token)){
            User user= new User(APPID, APPSECRET, true, true,
                    true, true,
                    AuthorityUtils.createAuthorityList("USER"));
            return Optional.of(user);
        }
        return  Optional.empty();
    }

    private synchronized void setTOKEN() {
        TOKEN = UUID.randomUUID().toString();
    }

    private void timeout() {
        while (true) {
            try {
                Thread.sleep(1800_000);
            } catch (InterruptedException e) {
            }
            setTOKEN();
        }
    }
}
