package time.api.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import time.api.service.SynonymesService;

import java.net.URISyntaxException;
import java.util.List;

@RestController
public class SynonymsRest {

    @Autowired
    private SynonymesService synonymesService;

    @RequestMapping(value = "/api/synonyms", method = RequestMethod.GET)
    public List<String> find(@RequestParam(value = "term", required = true) String term) throws URISyntaxException {
        return synonymesService.get(term);
    }

}