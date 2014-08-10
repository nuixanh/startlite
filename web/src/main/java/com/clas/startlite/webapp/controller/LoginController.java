package com.clas.startlite.webapp.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Son on 8/6/14.
 */
@Controller
public class LoginController extends ApplicationObjectSupport {
    private transient final Log log = LogFactory.getLog(LoginController.class);
    private String LIST_VIEW = "merchant/list";

    @RequestMapping(value="/login.html", method = RequestMethod.GET)
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("login");
        return mav;
    }
    @RequestMapping(value="/add-merchant.html", method = RequestMethod.GET)
    public ModelAndView auth(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView(LIST_VIEW);
        System.out.println("");
        return mav;
    }
}
