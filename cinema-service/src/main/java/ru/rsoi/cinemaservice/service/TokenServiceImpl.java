package ru.rsoi.cinemaservice.service;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    private String TOKEN = UUID.randomUUID().toString();
    private final String APPID = "49801d29-a24c-4b8e-88ca-2737575f9408";
    private final String APPSECRET = "f2d548f9-46bb-42a2-926b-aecc87c21518";

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
