package time.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import time.repo.bean.Phrase;
import time.web.bean.BucketsDTO;
import time.web.enums.Scale;
import time.web.enums.Sens;
import time.web.service.BucketService;
import time.web.service.IndexService;
import time.web.service.PhraseService;

@RestController
@ResponseBody
public class TimeController {

    @Autowired
    private IndexService indexService;

    @Autowired
    private BucketService bucketService;

    @Autowired
    private PhraseService phraseService;

    @RequestMapping(value = "/reindex", method = RequestMethod.GET)
    public String rebuildIndex() {
        return indexService.reIndex();
    }

    @RequestMapping(value = "/subbuckets", method = RequestMethod.GET)
    public BucketsDTO facets(@RequestParam(value = "scale", required = true) Scale scale, @RequestParam(value = "bucket", required = false) Long parentBucket, @RequestParam(value = "filter", required = false, defaultValue = "") String word) {
        return bucketService.getSubBuckets(scale, parentBucket, word);
    }

    @RequestMapping(value = "/phrases", method = RequestMethod.GET)
    public List<Phrase> find(@RequestParam(value = "scale", required = true) Scale scale, @RequestParam(value = "bucket", required = false) Long bucket, @RequestParam(value = "word", required = false) String word, @RequestParam(value = "page", required = false) Long page,
            @RequestParam(value = "sens", required = false) Sens sens) {

        return phraseService.find(scale, bucket, word, sens, page);
    }

}