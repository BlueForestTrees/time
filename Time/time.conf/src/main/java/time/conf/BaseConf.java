package time.conf;

import java.util.HashMap;
import java.util.Map;

public class BaseConf {
	private final static String arraySeparator = "/";
	protected final Map<String,Object> map = new HashMap<>();
	
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
	
	protected String[] asstringarray(final String key) {
		return asstring(key).split(arraySeparator);
	}
	
	protected String asstring(final String key){
		String string = asstring(key, null);
		if(string == null){
			throw new RuntimeException(key + " is null");
		}
		return string;
	}
	
	protected String asstring(final String key, final String def){
		String string = (String)map.get(key);
		if(string == null){
			string = def;
		}
		return string;
	}
	
	protected int asint(final String key, int def){
		return Integer.parseInt(asstring(key, Integer.toString(def)));
	}
	protected int asint(final String key){
		return Integer.parseInt(asstring(key));
	}
	protected boolean asbool(final String key){
		return Boolean.parseBoolean(asstring(key));
	}
	protected boolean asbool(final String key, final boolean def){
		return Boolean.parseBoolean(asstring(key, Boolean.toString(def)));
	}
	
	protected long aslong(final String key) {
		return Long.parseLong(asstring(key));
	}
	protected long aslong(final String key, final long def) {
		return Long.parseLong(asstring(key, Long.toString(def)));
	}

}
