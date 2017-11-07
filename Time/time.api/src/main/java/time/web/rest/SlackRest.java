package time.web.rest;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import time.web.service.SlackService;

@RestController
public class SlackRest {

    @Autowired
    private SlackService slackService;

    /*
     * Token slack gzPz99SDGcbEhzIfTkVKuCRf
     */
    @RequestMapping(value = "/api/first", method = RequestMethod.POST)
    public String slackFirstPost(@RequestParam final String text) throws IOException {
        return slackService.findFirstSlack(text);
    }

    @RequestMapping(value = "/api/first", method = RequestMethod.GET)
    public String slackFirstGet(@RequestParam final String text) throws IOException {
        return slackService.findFirstSlack(text);
    }

    @RequestMapping(value = "/api/last", method = RequestMethod.POST)
    public String slackLastPost(@RequestParam final String text) throws IOException {
        return slackService.findLastSlack(text);
    }

    @RequestMapping(value = "/api/last", method = RequestMethod.GET)
    public String slackLastGet(@RequestParam final String text) throws IOException {
        return slackService.findLastSlack(text);
    }

    @RequestMapping(value = "/api/random", method = RequestMethod.POST)
    public String slackRandomPost(@RequestParam final String text) throws IOException {
        return slackService.findRandomSlack(text);
    }

    @RequestMapping(value = "/api/random", method = RequestMethod.GET)
    public String slackRandomGet(@RequestParam final String text) throws IOException {
        return slackService.findRandomSlack(text);
    }

}