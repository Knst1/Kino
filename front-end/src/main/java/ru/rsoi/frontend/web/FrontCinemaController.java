package ru.rsoi.frontend.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.rsoi.frontend.model.*;
import ru.rsoi.frontend.services.FrontService;

import java.security.Principal;
import java.util.HashMap;

@Controller
class FrontCinemaController {

    private final FrontService service;

    @Autowired
    public FrontCinemaController(FrontService service) {
        this.service = service;
    }

    @GetMapping("/profile") // заглушка для зарезервированной страницы
    public String profile() {
        return "404";
    }

    @GetMapping({"/", "/index"})
    public String confirmRent(Model model, Principal principal) {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) principal;
        HashMap map = token.getPrincipal().getAttribute("principal");
        Integer cinemaId = (Integer) map.get("id");
        Cinema cinema = service.getCinema(cinemaId);
        if (cinema == null)
            return "503";
        model.addAttribute("cinema", cinema);
        return "index";
    }


}
