package time.web.transformer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.lucene.facet.FacetResult;
import org.apache.lucene.facet.LabelAndValue;
import org.springframework.stereotype.Component;

import time.web.bean.Bucket;
import time.web.bean.Buckets;
import time.web.enums.Scale;

@Component
public class BucketTransformer {

    public Buckets toBucketsDTO(final FacetResult facetResult, final Scale scale, final Long parentBucket) {
        final Buckets bucketsDTO = new Buckets();
        bucketsDTO.setSubbuckets(toBucketsDTO(facetResult));
        bucketsDTO.setParentBucket(parentBucket);
        bucketsDTO.setScale(scale);
        return bucketsDTO;
    }

    public List<Bucket> toBucketsDTO(final FacetResult facetResult) {
        return Arrays.stream(facetResult.labelValues).map(elt -> toBucketDTO(elt)).collect(Collectors.toList());
    }

    public Bucket toBucketDTO(LabelAndValue facet) {
        final Bucket facetDTO = new Bucket();
        facetDTO.setBucket(new Long(facet.label));
        facetDTO.setCount((int) facet.value);
        return facetDTO;
    }
}
