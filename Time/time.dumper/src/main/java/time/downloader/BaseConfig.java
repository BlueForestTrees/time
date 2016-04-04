package time.downloader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BaseConfig {
	private final Properties prop = new Properties();
	/**
	 * Construit une map à partir des infos
	 * /base/path
	 *			\_sources
	 *				\_[name]
	 *					\_.conf
	 *			\_phrases
	 *			\_indexes
	 * 
	 * @param basePath
	 * @param name
	 * @throws IOException 
	 */
	public BaseConfig(final String basePath, final String name) throws IOException{
		final String sourcesPath = basePath + (!basePath.endsWith("/")?"/":"") + "sources/";
		final String tempPath = basePath + (!basePath.endsWith("/")?"/":"") + "temp/";
		final String indexesPath = basePath + (!basePath.endsWith("/")?"/":"") + "indexes/";
		final String confPath = sourcesPath + name + "/" + ".conf";
		final InputStream in = getClass().getResourceAsStream(confPath);
		prop.load(in);
		in.close();
		prop.setProperty("sourcesPath", sourcesPath);
		prop.setProperty("tempPath", tempPath);
		prop.setProperty("indexesPath", indexesPath);
		prop.setProperty("basePath", basePath);
		prop.setProperty("name", name);
	}
	
	@Override
	public String toString(){
		final StringBuilder sb = new StringBuilder();

        sb.append("\n");
        sb.append("\n----------CONFIG-------------");
        prop.entrySet().forEach(e -> sb.append("\n " + e.getKey() + " = " + e.getValue()));
        sb.append("\n--------END CONFIG-----------");

        return sb.toString();
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
	
	protected long aslong(final String key) {
		return Long.parseLong(asstring(key));
	}
	protected long aslong(final String key, final long def) {
		return Long.parseLong(asstring(key, Long.toString(def)));
	}

}
