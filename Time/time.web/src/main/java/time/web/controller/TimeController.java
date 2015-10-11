package time.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import time.repo.bean.Phrase;
import time.web.bean.FacetsDTO;
import time.web.enums.Scale;
import time.web.enums.Sens;
import time.web.service.FacetService;
import time.web.service.IndexService;
import time.web.service.PhraseService;

@RestController
@ResponseBody
public class TimeController {

	@Autowired
	private IndexService indexService;
	
	@Autowired
	private FacetService facetService;
	
	@Autowired
	private PhraseService phraseService;

	@RequestMapping(value = "/reindex", method = RequestMethod.GET)
	public String rebuildIndex() {
		return indexService.reIndex();
	}
	
	@RequestMapping(value = "/facets", method = RequestMethod.GET)
	public FacetsDTO facets(@RequestParam(value = "scale", required = true) Scale scale,
			@RequestParam(value = "bucket", required = false) Long bucket,
			@RequestParam(value = "word", required = false, defaultValue = "") String word){
		return facetService.timeFacetsDTO(scale, bucket, word);
	}

	@RequestMapping(value = "/phrases", method = RequestMethod.GET)
	public List<Phrase> find(
			@RequestParam(value = "scale", required = true) Scale scale,
			@RequestParam(value = "bucket", required = false) Long bucket,
			@RequestParam(value = "word", required = false) String word,
			@RequestParam(value = "page", required = false) Long page,
			@RequestParam(value = "sens", required = false) Sens sens) throws Exception {

		return phraseService.find(scale, bucket, word, sens, page);
	}


}