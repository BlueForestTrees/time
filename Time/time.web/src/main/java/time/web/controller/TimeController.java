package time.web.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import time.web.bean.Buckets;
import time.web.bean.Phrases;
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

    @RequestMapping(value = "/buckets", method = RequestMethod.GET)
    public Buckets getBuckets(@RequestParam(value = "scale", required = true) Scale scale, 
            @RequestParam(value = "bucket", required = false) Long bucket, 
            @RequestParam(value = "filter", required = false, defaultValue = "") String filter) throws IOException {
        return bucketService.getBuckets(scale, bucket, filter);
    }    
    
    @RequestMapping(value = "/phrases", method = RequestMethod.GET)
    public Phrases find(@RequestParam(value = "scale", required = true) Scale scale, 
            @RequestParam(value = "bucket", required = false) Long bucket, 
            @RequestParam(value = "word", required = false) String word, 
            @RequestParam(value="doc", required=false)Integer doc,
            @RequestParam(value="score", required=false)Float score,
            @RequestParam(value="lastIndex", required=false)Integer lastIndex,
            @RequestParam(value = "sens", required = false) Sens sens) throws IOException {

        return phraseService.find(scale, bucket, word, doc, score, lastIndex);
    }

}