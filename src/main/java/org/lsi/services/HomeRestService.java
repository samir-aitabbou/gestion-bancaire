package org.lsi.services;

import org.lsi.entities.Client;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeRestService {

    @RequestMapping("/")
    public String aff(){

        return "Home/index";
    }
}