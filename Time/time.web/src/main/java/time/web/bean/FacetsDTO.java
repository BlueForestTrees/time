package time.web.bean;

import java.util.List;

public class FacetsDTO {
	
	private Long page;
	
	public Long getPage() {
		return page;
	}

	public void setPage(Long page) {
		this.page = page;
	}

	private List<FacetDTO> facets;

	public List<FacetDTO> getFacets() {
		return facets;
	}

	public void setFacets(List<FacetDTO> facets) {
		this.facets = facets;
	}
}
