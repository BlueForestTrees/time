package time.web.transformer;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.search.query.facet.Facet;
import org.springframework.stereotype.Component;

import time.web.bean.FacetDTO;

@Component
public class FacetTransformer {
	public List<FacetDTO> facetToFacetDTO(List<Facet> facets){
		return facets.stream().map(elt -> facetToFacetDTO(elt)).collect(Collectors.toList());
	}
	
	public FacetDTO facetToFacetDTO(Facet facet){
		final FacetDTO facetDTO = new FacetDTO();
		facetDTO.setDate(new Long(facet.getValue()));
		facetDTO.setCount(facet.getCount());
		return facetDTO;
	}
}
