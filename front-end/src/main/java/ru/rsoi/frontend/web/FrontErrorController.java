package ru.rsoi.frontend.web;

import org.slf4j.Logger;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
class FrontErrorController implements ErrorController {
    private static final Logger logger = getLogger(FrontCinemaController.class);
    private final static String ERROR_PATH = "/error";

    @RequestMapping(ERROR_PATH)
    public String error(HttpServletRequest request) {
        HttpStatus status = getStatus(request);
        logger.info("Ошибка {}", status.value());
       if (status.value() == 400)
           return "400";
        return "404";
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request
                .getAttribute("javax.servlet.error.status_code");
        if (statusCode != null) {
            try {
                return HttpStatus.valueOf(statusCode);
            }
            catch (Exception ex) {
            }
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
