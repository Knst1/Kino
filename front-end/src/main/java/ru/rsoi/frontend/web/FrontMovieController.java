package ru.rsoi.frontend.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.rsoi.frontend.model.*;
import ru.rsoi.frontend.services.FrontService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
class FrontMovieController {

    private final FrontService service;
    private static Map<String, Object> rentModel;

    @Autowired
    public FrontMovieController(FrontService service) {
        this.service = service;
        rentModel = new HashMap<>();
    }

    @GetMapping("/movie/{movieId}")
    public String getMovie(@PathVariable Integer movieId, Model model) {
        Movie movie = service.getMovie(movieId);
        if (movie == null)
            return "503";
        if (movie.getName() == null)
            return "404";
        model.addAttribute("rent", Boolean.FALSE);
        for(Map.Entry<String, Object> item : rentModel.entrySet()){
            model.addAttribute(item.getKey(), item.getValue());
        }
        model.addAttribute("movie", movie);
        rentModel = new HashMap<>();
        return "movie";
    }

    @GetMapping(path = "/movies", params = {"page", "size"})
    public String getMovie(Model model, @RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        List<ShortMovie> moviet, movies = new ArrayList<>();
        Boolean left = false, right = false;
        if (size < 1 || size > 99)
            size = 21;
        if (page < 1)
            page = 1;
        if (page > 1) {
            moviet = service.getMovies(page, size);
            if (moviet == null)
                return "503";
            if (moviet.size() > 0) {
                movies.addAll(moviet);
                moviet = service.getMovies(page + 1, size);
                if (moviet == null)
                    return "503";
                right = moviet.size() > 0;
            }
            else
                page = 1;
        }
        if (page == 1) {
            moviet = service.getMovies(1, 3 * size);
            if (moviet == null)
                return "503";
            left = (moviet.size() - 1) / size > 0;
            right = (moviet.size() - 1) / size > 1;
            movies = moviet.subList(0, Math.min(size, moviet.size()));
        }
        model.addAttribute("movies", movies);
        model.addAttribute("left", left);
        model.addAttribute("right", right);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("realsize", movies.size());
        return "movies";
    }

    @PostMapping(path = "/rent")
    public String addRent(Model model, Principal principal, @RequestParam Integer movieId, @RequestParam Integer duration) {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) principal;
        HashMap map = token.getPrincipal().getAttribute("principal");
        Integer cinemaId = (Integer) map.get("id");
        RentInfoResponse rent = service.addRent(new AddRentInfoBody(cinemaId, movieId, duration));
        if (duration < 1 || duration > 1000)
            return "400";
        Boolean ok = rent!=null;
        rentModel.put("ok", ok);
        if (ok) {
            rentModel.put("duration", rent.getDuration());
        }
        rentModel.put("rent", Boolean.TRUE);
        for(Map.Entry<String, Object> item : rentModel.entrySet()){
            model.addAttribute(item.getKey(), item.getValue());
        }
        return "redirect:/movie/" + movieId.toString();
    }
}
