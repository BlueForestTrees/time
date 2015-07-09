package wiki.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import wiki.entity.Phrase;
import wiki.enums.Sort;
import wiki.service.PhraseService;

@RestController
@ResponseBody
public class TimeController {

	@Autowired
	private PhraseService phraseService;

	@RequestMapping(value="/time/find/{sort}", method = RequestMethod.GET)
	public List<Phrase> find(
			@RequestParam(value = "date", required=false) Long date,
			@RequestParam(value = "word", required=false) String word,
			@PathVariable Sort sort) throws Exception {
		
		return phraseService.find(date, word, sort);
	}
	
	@RequestMapping(value="/time/reindex", method = RequestMethod.GET)
	public String rebuildIndex() {
		return phraseService.reIndex();
	}
}