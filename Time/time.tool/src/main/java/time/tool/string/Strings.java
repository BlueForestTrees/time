package time.tool.string;

/**
 * Created by slimane_medini on 28/04/2016.
 */
public class Strings {
    public static String withSlash(final String begin) {
        return with(begin, "/");
    }
    public static String withPipe(final String begin) {
        return with(begin, "|");
    }
    public static String with(String begin, final String end){
        if(begin == null){
            return "";
        }else if(begin.endsWith(end)){
            return begin;
        }else{
            return begin + end;
        }
    }
}
