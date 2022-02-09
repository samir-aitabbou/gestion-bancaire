package org.lsi.services;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginReService {

    @RequestMapping(value = "/Login")
    public String log(){
        return "Login/Login";
    }

}
