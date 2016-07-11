package time.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import time.web.bean.BucketGroup;
import time.web.service.BucketService;

import java.io.IOException;

@RestController
public class BucketRest {

    @Autowired
    private BucketService bucketService;

    /**
     * @param term recherche time
     * @return Renvoie les buckets d'une recherche et d'une échelle donnée.
     * @throws IOException
     */
    @RequestMapping(value = "/api/buckets", method = RequestMethod.GET)
    public BucketGroup getBuckets(@RequestParam(value = "term", required = false) String term) throws IOException {
        return bucketService.getBuckets(term);
    }

}