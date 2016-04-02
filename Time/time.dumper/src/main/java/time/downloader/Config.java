package time.downloader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	private final Properties prop = new Properties();
	/**
	 * Construit une map à partir des infos
	 * /base/path
	 *			\_sources
	 *				\_[name]
	 *					\_.conf
	 *			\_phrases
	 *			\_index
	 * 
	 * @param basePath
	 * @param name
	 * @throws IOException 
	 */
	public Config(final String basePath, final String name) throws IOException{
		final String confPath = basePath + (!basePath.endsWith("/")?"/":"") + name + "/.conf";
		final InputStream in = getClass().getResourceAsStream(confPath);
		prop.load(in);
		in.close();
	}
	
	public String asstring(final String key){
		return asstring(key, null);
	}
	
	public String asstring(final String key, final String def){
		return prop.getProperty(key, def);
	}
	
	public int asint(final String key, int def){
		return Integer.parseInt(asstring(key, Integer.toString(def)));
	}
	public int asint(final String key){
		return Integer.parseInt(asstring(key));
	}
	public boolean asbool(final String key){
		return Boolean.parseBoolean(asstring(key));
	}
	public boolean asbool(final String key, final boolean def){
		return Boolean.parseBoolean(asstring(key, Boolean.toString(def)));
	}
}
