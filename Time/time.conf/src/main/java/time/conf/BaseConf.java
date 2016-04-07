package time.conf;

import java.util.HashMap;
import java.util.Map;

public class BaseConf {
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
	
	public String asstring(final String key){
		return asstring(key, null);
	}
	
	public String asstring(final String key, final String def){
		final String string = (String)map.get(key);
		if(string == null){
			throw new RuntimeException(key + " is null");
		}
		return string;
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
