package time.repo;

import java.util.List;

import org.apache.lucene.demo.facet.SimpleFacetsExample;
import org.apache.lucene.facet.FacetResult;
import org.junit.Test;

public class TestFacets {

	@Test
	public void test() throws Exception {
	    System.out.println("Facet counting example:");
	    System.out.println("-----------------------");
	    SimpleFacetsExample example = new SimpleFacetsExample();
	    List<FacetResult> results1 = example.runFacetOnly();
	    System.out.println("Author: " + results1.get(0));
	    System.out.println("Publish Date: " + results1.get(1));
	    
	    System.out.println("Facet counting example (combined facets and search):");
	    System.out.println("-----------------------");
	    List<FacetResult> results = example.runSearch();
	    System.out.println("Author: " + results.get(0));
	    System.out.println("Publish Date: " + results.get(1));
	    
	    System.out.println("Facet drill-down example (Publish Date/2010):");
	    System.out.println("---------------------------------------------");
	    System.out.println("Author: " + example.runDrillDown());

	    System.out.println("Facet drill-sideways example (Publish Date/2010):");
	    System.out.println("---------------------------------------------");
	    for(FacetResult result : example.runDrillSideways()) {
	      System.out.println(result);
	    }
	}

}
