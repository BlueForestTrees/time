package time.web.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import time.web.service.FindBetterService;

@Controller
@PreAuthorize("hasIpAddress('127.0.1.1')")
public class AdminController {


    private static final Logger LOGGER = LogManager.getLogger(AdminController.class);

    @Autowired
    WebApplicationContext context;

    @RequestMapping(value = "/api/admin/refresh", method = RequestMethod.GET)
    public String refresh(){
        LOGGER.info("context.refresh(). . .");
        ((AnnotationConfigWebApplicationContext)context).refresh();
        LOGGER.info("context.refresh() done.");
        return "context refreshed";
    }
    
}
