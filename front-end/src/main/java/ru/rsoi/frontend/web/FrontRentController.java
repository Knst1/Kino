package ru.rsoi.frontend.web;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.rsoi.frontend.model.RentInfoResponse;
import ru.rsoi.frontend.services.FrontService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
class FrontRentController {

    private final FrontService service;
    private static String currentList;

    @Autowired
    public FrontRentController(FrontService service) {
        this.service = service;
        currentList = "/prerents?page=1&size=21";
    }

    @GetMapping(path = "/allrents", params = {"page", "size"})
    public String getAllMovies(Model model, Principal principal, @RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        List<RentInfoResponse> rentt, rents = new ArrayList<>();
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) principal;
        HashMap map = token.getPrincipal().getAttribute("principal");
        Integer cinemaId = (Integer) map.get("id");
        Boolean left = false, right = false;
        if (size < 1 || size > 99)
            size = 21;
        if (page < 1)
            page = 1;
        if (page > 1) {
            rentt = service.getAllMovies(cinemaId, page, size);
            if (rentt == null)
                return "503";
            if (rentt.size() > 0) {
                rents.addAll(rentt);
                rentt = service.getAllMovies(cinemaId, page + 1, size);
                if (rentt == null)
                    return "503";
                right = rentt.size() > 0;
            }
            else
                page = 1;
        }
        if (page == 1) {
            rentt = service.getAllMovies(cinemaId, 1, 3 * size);
            if (rentt == null)
                return "503";
            left = (rentt.size() - 1) / size > 0;
            right = (rentt.size() - 1) / size > 1;
            rents = rentt.subList(0, Math.min(size, rentt.size()));
        }
        currentList = "/allrents?page=" + page.toString() + "&size=" + size.toString();
        for(RentInfoResponse rent:rents)
            rent.setMovieAccess(rent.getMovie() != null);
        model.addAttribute("rents", rents);
        model.addAttribute("left", left);
        model.addAttribute("right", right);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("realsize", rents.size());
        return "allrents";
    }

    @GetMapping(path = "/prerents", params = {"page", "size"})
    public String getPrerentMovies(Model model, Principal principal, @RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        List<RentInfoResponse> rentt, rents = new ArrayList<>();
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) principal;
        HashMap map = token.getPrincipal().getAttribute("principal");
        Integer cinemaId = (Integer) map.get("id");
        Boolean left = false, right = false;
        if (size < 1 || size > 99)
            size = 21;
        if (page < 1)
            page = 1;
        if (page > 1) {
            rentt = service.getPrerentMovies(cinemaId, page, size);
            if (rentt == null)
                return "503";
            if (rentt.size() > 0) {
                rents.addAll(rentt);
                rentt = service.getPrerentMovies(cinemaId, page + 1, size);
                if (rentt == null)
                    return "503";
                right = rentt.size() > 0;
            }
            else
                page = 1;
        }
        if (page == 1) {
            rentt = service.getPrerentMovies(cinemaId, 1, 3 * size);
            if (rentt == null)
                return "503";
            left = (rentt.size() - 1) / size > 0;
            right = (rentt.size() - 1) / size > 1;
            rents = rentt.subList(0, Math.min(size, rentt.size()));
        }
        currentList = "/prerents?page=" + page.toString() + "&size=" + size.toString();
        for(RentInfoResponse rent:rents)
            rent.setMovieAccess(rent.getMovie() != null);
        model.addAttribute("rents", rents);
        model.addAttribute("left", left);
        model.addAttribute("right", right);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("realsize", rents.size());
        return "prerents";
    }

    @GetMapping(path = "/rents", params = {"page", "size"})
    public String getRentMovies(Model model, Principal principal, @RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        List<RentInfoResponse> rentt, rents = new ArrayList<>();
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) principal;
        HashMap map = token.getPrincipal().getAttribute("principal");
        Integer cinemaId = (Integer) map.get("id");
        Boolean left = false, right = false;
        if (size < 1 || size > 99)
            size = 21;
        if (page < 1)
            page = 1;
        if (page > 1) {
            rentt = service.getRentMovies(cinemaId, page, size);
            if (rentt == null)
                return "503";
            if (rentt.size() > 0) {
                rents.addAll(rentt);
                rentt = service.getRentMovies(cinemaId, page + 1, size);
                if (rentt == null)
                    return "503";
                right = rentt.size() > 0;
            }
            else
                page = 1;
        }
        if (page == 1) {
            rentt = service.getRentMovies(cinemaId, 1, 3 * size);
            if (rentt == null)
                return "503";
            left = (rentt.size() - 1) / size > 0;
            right = (rentt.size() - 1) / size > 1;
            rents = rentt.subList(0, Math.min(size, rentt.size()));
        }
        for(RentInfoResponse rent:rents)
            rent.setMovieAccess(rent.getMovie() != null);
        model.addAttribute("rents", rents);
        model.addAttribute("left", left);
        model.addAttribute("right", right);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("realsize", rents.size());
        return "rents";
    }

    @PostMapping(path = "/duration")
    public String setDuration(Model model, Principal principal, @RequestParam Integer rentId, @RequestParam Integer duration) {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) principal;
        HashMap map = token.getPrincipal().getAttribute("principal");
        Integer cinemaId = (Integer) map.get("id");
        Boolean success = service.setDuration(cinemaId, rentId, duration);
        if (success == null)
            return "503";
        if (!success)
            return "404";
        return "redirect:" + currentList;
    }

    @PostMapping(path = "/confirm")
    public String confirmRent(Model model, Principal principal, @RequestParam Integer rentId) {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) principal;
        HashMap map = token.getPrincipal().getAttribute("principal");
        Integer cinemaId = (Integer) map.get("id");
        Boolean success = service.confirmRent(cinemaId, rentId);
        if (success == null)
            return "503";
        if (!success)
            return "404";
        return "redirect:/rents?page=1&size=21";
    }

    @PostMapping(path = "/delete")
    public String deleteRent(Model model, Principal principal, @RequestParam Integer rentId) {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) principal;
        HashMap map = token.getPrincipal().getAttribute("principal");
        Integer cinemaId = (Integer) map.get("id");
        Boolean success = service.deleteRent(cinemaId, rentId);
        if (success == null)
            return "503";
        if (!success)
            return "404";
        return "redirect:" + currentList;
    }
}
