package com.kenny.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class SSOController {

    final static Logger logger = LoggerFactory.getLogger(SSOController.class);

    @GetMapping("/hello")
    @ResponseBody
    public Object hello() {

        return "</br></br></br><H1>SSO - Hello World!</H1>";
    }
}
