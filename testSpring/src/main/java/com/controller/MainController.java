package com.controller;

import com.customBean.TestBean;
import com.customBean.TestContextBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/*")
public class MainController {

    @Autowired
    private TestBean testBean;

    @RequestMapping( method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {
        model.addAttribute("message", "Spring 3 MVC - Car Service");
        return "hello";
    }

    @RequestMapping (value="sayHello", method = RequestMethod.GET)
    public String sayHello(ModelMap model){
        model.addAttribute("message", testBean.sayHello());
        return "hello";

    }

    @RequestMapping (value="contextHello", method = RequestMethod.GET)
    public String contextHello(ModelMap model){
        ApplicationContext aplicntxt = new ClassPathXmlApplicationContext("beans.xml");
            TestContextBean contextBean = (TestContextBean) aplicntxt.getBean("testContextBean");
        model.addAttribute("message", contextBean.sayHello());
        return "hello";

    }
}
