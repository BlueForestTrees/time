package time.web.rest;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import time.web.service.PhraseService;

@RestController
public class SlackRest {

    @Autowired
    private PhraseService phraseService;

    /*
     * Token slack gzPz99SDGcbEhzIfTkVKuCRf
     */
    /**
     * Slack endpoint
     * @param text
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/api/first", method = RequestMethod.POST)
    public String slackFirstPost(@RequestParam final String text) throws IOException {
        return phraseService.findFirstSlack(text);
    }

    @RequestMapping(value = "/api/first", method = RequestMethod.GET)
    public String slackFirstGet(@RequestParam final String text) throws IOException {
        return phraseService.findFirstSlack(text);
    }

    @RequestMapping(value = "/api/random", method = RequestMethod.POST)
    public String slackRandomPost(@RequestParam final String text) throws IOException {
        return phraseService.findRandomSlack(text);
    }

    @RequestMapping(value = "/api/random", method = RequestMethod.GET)
    public String slackRandomGet(@RequestParam final String text) throws IOException {
        return phraseService.findRandomSlack(text);
    }

}