package ru.rsoi.movieservice.service;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    private String TOKEN = UUID.randomUUID().toString();
    private final String APPID = "c119621b-21f9-4305-a90c-5df5c5d0150e";
    private final String APPSECRET = "168f38d0-f3aa-4e98-ab3b-10d7a78ef279";

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
