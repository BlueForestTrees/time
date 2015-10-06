package time.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import time.repo.bean.Phrase;
import time.web.bean.FacetsDTO;
import time.web.enums.Scale;
import time.web.enums.Sens;
import time.web.service.PhraseService;

@RestController
@ResponseBody
public class TimeController {

	@Autowired
	private PhraseService phraseService;

	@RequestMapping(value = "/find/{sens}", method = RequestMethod.GET)
	public List<Phrase> find(@RequestParam(value = "date", required = false) Long date, @RequestParam(value = "word", required = false) String word,
			@RequestParam(value = "page", required = false) Long page, @PathVariable Sens sens) throws Exception {

		return phraseService.find(date, word, sens, page);
	}

	@RequestMapping(value = "/reindex", method = RequestMethod.GET)
	public String rebuildIndex() {
		return phraseService.reIndex();
	}

	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/facets", method = RequestMethod.GET)
	public FacetsDTO facets(@RequestParam(value = "scale", required = true) Scale scale,
							  @RequestParam(value = "word", required = false, defaultValue = "") String word, 
							  @RequestParam(value = "page", required = false, defaultValue = "0") Long page) {
		return phraseService.timeFacetsDTO(scale, word, page);
	}


}