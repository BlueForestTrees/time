package time.crawler.write.tika;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;

public class MyTikaMetadata {
		private final String identifier;
		private final String title;
		private final String creator;
		private final String created;
		private final String language;
		private final String type;
		private final String comments;
		
		public MyTikaMetadata(final Metadata metadata) {
			identifier = metadata.get(TikaCoreProperties.IDENTIFIER);
			title = metadata.get(TikaCoreProperties.TITLE);
			creator = metadata.get(TikaCoreProperties.CREATOR);
			created = metadata.get(TikaCoreProperties.CREATED);
			language = metadata.get(TikaCoreProperties.LANGUAGE);
			type = metadata.get(TikaCoreProperties.TYPE);
			comments = metadata.get(TikaCoreProperties.COMMENTS);
		}

		public String getTitle() {
			return title;
		}

		public String getCreator() {
			return creator;
		}

		public String getCreated() {
			return created;
		}

		public String getLanguage() {
			return language;
		}

		public String getType() {
			return type;
		}

		public String getComments() {
			return comments;
		}

		public String getIdentifier() {
			return identifier;
		}
	}