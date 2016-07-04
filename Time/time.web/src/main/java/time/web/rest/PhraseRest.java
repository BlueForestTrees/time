package time.web.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import time.web.bean.Phrases;
import time.web.service.PhraseService;

import java.io.IOException;

@RestController
public class PhraseRest {

    private static final Logger LOGGER = LogManager.getLogger(PhraseRest.class);

    @Autowired
    private PhraseService phraseService;

    @RequestMapping(value = "/api/phrases", method = RequestMethod.GET)
    public Phrases find(
            @RequestParam(value = "request", required = false) String request,
            @RequestParam(value = "field", required = false) String field,
            @RequestParam(value = "from", required = false) Long from,
            @RequestParam(value = "to", required = false) Long to,
            @RequestParam(value = "lastKey", required = false) String lastKey) throws IOException {
        return phraseService.find(request, field, from, to, lastKey);
    }

}
