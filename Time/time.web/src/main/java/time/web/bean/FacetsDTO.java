package time.web.bean;

import java.util.List;

import time.web.enums.Scale;

public class FacetsDTO {
	
	private Scale scale;
	
	public Scale getScale() {
		return scale;
	}

	public void setScale(Scale scale) {
		this.scale = scale;
	}

	private List<FacetDTO> facets;

	public List<FacetDTO> getFacets() {
		return facets;
	}

	public void setFacets(List<FacetDTO> facets) {
		this.facets = facets;
	}
	
	private Long bucket;

	public void setBucket(Long bucket) {
		this.bucket = bucket;
	}

	public Long getBucket() {
		return bucket;
	}

}
