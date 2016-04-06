package time.tool.conf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BaseContext {
	protected final Map<String,Object> map = new HashMap<>();
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
	public BaseContext() {
//		final String basePath = "chemin";
//		final String name = "wiki";
//		final String sourcesPath = basePath + (!basePath.endsWith("/")?"/":"") + "sources/";
//		final String tempPath = basePath + (!basePath.endsWith("/")?"/":"") + "temp/";
//		final String indexesPath = basePath + (!basePath.endsWith("/")?"/":"") + "indexes/";
//		final String confPath = sourcesPath + name + "/" + ".conf";
//		final InputStream in = getClass().getResourceAsStream(confPath);
//		prop.load(in);
//		in.close();
//		prop.setProperty("sourcesPath", sourcesPath);
//		prop.setProperty("tempPath", tempPath);
//		prop.setProperty("indexesPath", indexesPath);
//		prop.setProperty("basePath", basePath);
//		prop.setProperty("name", name);
	}
	
	public void put(final String key, final Object value){
		map.put(key, value);
	}
	
	@Override
	public String toString(){
		final StringBuilder sb = new StringBuilder();

        sb.append("\n");
        sb.append("\n----------CONFIG-------------");
        map.entrySet().forEach(e -> sb.append("\n " + e.getKey() + " = " + e.getValue()));
        sb.append("\n--------END CONFIG-----------");

        return sb.toString();
	}
	
	public String asstring(final String key){
		return asstring(key, null);
	}
	
	public String asstring(final String key, final String def){
		return (String)map.get(key);
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
