package com.comparador.ComparadorTI.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/test-websocket")
    public String testWebsocketPage() {
        return "test-websocket";
    }
}