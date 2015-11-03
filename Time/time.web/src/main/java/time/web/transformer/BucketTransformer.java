package time.web.transformer;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.search.query.facet.Facet;
import org.springframework.stereotype.Component;

import time.web.bean.BucketDTO;
import time.web.bean.BucketsDTO;
import time.web.enums.Scale;

@Component
public class BucketTransformer {
	
	public BucketsDTO toBucketsDTO(final List<Facet> facets, final Scale scale, final Long parentBucket){
		final BucketsDTO bucketsDTO = new BucketsDTO();
		bucketsDTO.setSubbuckets(toBucketsDTO(facets));
		bucketsDTO.setParentBucket(parentBucket);
		bucketsDTO.setScale(scale);
		return bucketsDTO;
	}
	
	public List<BucketDTO> toBucketsDTO(List<Facet> facets){
		return facets.stream().map(elt -> toBucketDTO(elt)).collect(Collectors.toList());
	}
	
	public BucketDTO toBucketDTO(Facet facet){
		final BucketDTO facetDTO = new BucketDTO();
		facetDTO.setBucket(new Long(facet.getValue()));
		facetDTO.setCount(facet.getCount());
		return facetDTO;
	}
}
