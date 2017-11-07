package time.api.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import time.domain.DatedPhrase;
import time.domain.Metadata;

@Service
public class SlackService
{

    private static final Logger LOGGER = LogManager.getLogger(PhraseService.class);

    @Autowired
    private PhraseService phraseService;

    public String findFirstSlack(final String term){
        return toSlackMessageFormat(phraseService.findFirst(term));
    }

    public String findLastSlack(final String term){
        return toSlackMessageFormat(phraseService.findLast(term));
    }

    public String findRandomSlack(String term) {
        return toSlackMessageFormat(phraseService.findRandom(term));
    }

    private String toSlackMessageFormat(final DatedPhrase phrase) {
        final String text = phrase.getText();
        final String author = phrase.getType() == Metadata.Type.WIKI ? "Wikipédia" : phrase.getAuthor();
        String slackFormattedMessage = text.replace("<strong> ", " <strong>").replace(" </strong>", "</strong> ").replace("<b> ", " <b>").replace(" </b>", "</b> ").replace("<B> ", " <B>").replace(" </B>", "</B> ").replace("<B>", "*").replace("</B>", "*").replace("<b>", "*").replace("</b>", "*").replace("<strong>", "*").replace("</strong>", "*");

        slackFormattedMessage += "  ("+author+")";

        return "{\"response_type\": \"in_channel\",\"text\":\""+slackFormattedMessage+"\"}";
    }

}
