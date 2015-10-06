package time.web.transformer;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.search.query.facet.Facet;
import org.springframework.stereotype.Component;

import time.web.bean.FacetDTO;
import time.web.bean.FacetsDTO;
import time.web.enums.Scale;

@Component
public class FacetTransformer {
	
	public FacetsDTO toFacetsDTO(List<Facet> facets, long page, Scale scale){
		final FacetsDTO facetsDTO = new FacetsDTO();
		facetsDTO.setFacets(toFacetDTO(facets, scale));
		facetsDTO.setPage(page);
		return facetsDTO;
	}
	
	public List<FacetDTO> toFacetDTO(List<Facet> facets, Scale scale){
		return facets.stream().map(elt -> toFacetDTO(elt, scale)).collect(Collectors.toList());
	}
	
	public FacetDTO toFacetDTO(Facet facet, Scale scale){
		final FacetDTO facetDTO = new FacetDTO();
		facetDTO.setDate(new Long(facet.getValue())*scale.getMultiplier());
		facetDTO.setCount(facet.getCount());
		return facetDTO;
	}
}
