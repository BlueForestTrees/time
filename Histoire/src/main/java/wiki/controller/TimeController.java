package wiki.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/time")
public class TimeController {
	
	@RequestMapping(method = RequestMethod.GET)
    public String start() throws Exception {

        return "OK!";
    }
	
	
}