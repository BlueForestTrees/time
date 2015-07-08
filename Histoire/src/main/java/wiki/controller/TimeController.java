package wiki.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import wiki.entity.Phrase;
import wiki.service.PhraseService;

@RestController
@RequestMapping("/time")
@ResponseBody
public class TimeController {

	@Autowired
	private PhraseService phraseService;

	@RequestMapping(method = RequestMethod.GET)
	public List<Phrase> getPhrasesListAroundDate(@RequestParam(value = "date", required = true) long date,
			@RequestParam(value = "pageSize", required = true) short pageSize, @RequestParam(value = "word", required = false) String word) throws Exception {

		return phraseService.find(date, pageSize, word);

	}

	@RequestMapping(value="/reindex", method = RequestMethod.GET)
	public String rebuildIndex() {
		return phraseService.rebuildIndex();
	}
}