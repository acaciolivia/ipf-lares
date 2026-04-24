package com.ipfnoslares.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpaFallbackController {

    @RequestMapping(value = "/{path:[^\\.]*}")
    public String redirectIndex() {
        return "forward:/index.html";
    }
}