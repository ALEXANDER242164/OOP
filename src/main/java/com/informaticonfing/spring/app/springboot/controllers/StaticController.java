package com.informaticonfing.spring.app.springboot.controllers;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping
public class StaticController {

    // Forward root to index.html in static
    @GetMapping({"/", ""})
    public String index() {
        return "forward:/index.html";
    }

    // Serve specifically diseno.css to avoid any encoding/path issues
    @GetMapping(value = "/diseno.css", produces = "text/css")
    public ResponseEntity<Resource> disenoCss() throws IOException {
        Resource css = new ClassPathResource("static/diseno.css");
        if (!css.exists()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "text/css; charset=UTF-8")
                .body(css);
    }

}
