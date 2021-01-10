package ru.rsoi.cinemaservice.web;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.rsoi.cinemaservice.service.TokenService;

@RestController
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @PostMapping("/token")
    public String getToken(@RequestParam("username") final String username, @RequestParam("password") final String password) {
       String token= tokenService.login(username,password);
       if(StringUtils.isEmpty(token)){
           return "";
       }
       return token;
    }
}
