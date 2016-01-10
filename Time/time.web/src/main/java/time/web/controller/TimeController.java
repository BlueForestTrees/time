package time.web.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import time.web.bean.BucketGroup;
import time.web.bean.Phrases;
import time.web.service.BucketService;
import time.web.service.IndexService;
import time.web.service.PhraseService;
import time.web.service.SynonymesService;

@RestController
public class TimeController {

    @Autowired
    private IndexService indexService;

    @Autowired
    private BucketService bucketService;

    @Autowired
    private PhraseService phraseService;

    @Autowired
    private SynonymesService synonymesService;

    @RequestMapping(value = "/api/reindex", method = RequestMethod.GET)
    public String rebuildIndex() {
        return indexService.reIndex();
    }

    @RequestMapping(value = "/api/buckets", method = RequestMethod.GET)
    public BucketGroup getBuckets(@RequestParam(value = "scale", required = true) String scale, @RequestParam(value = "term", required = false) String term) throws IOException {
        return bucketService.getBuckets(scale, term);
    }

    @RequestMapping(value = "/api/phrases", method = RequestMethod.GET)
    public Phrases find(@RequestParam(value = "scale", required = false) String scale, @RequestParam(value = "bucket", required = false) Long bucket, @RequestParam(value = "term", required = false) String term, @RequestParam(value = "lastKey", required = false) String lastKey) throws IOException {
        return phraseService.find(scale, bucket, term, lastKey);
    }

    @RequestMapping(value = "/api/synonyms", method = RequestMethod.GET)
    public List<String> find(@RequestParam(value = "term", required = true) String term) throws URISyntaxException {
        return synonymesService.get(term);
    }

}