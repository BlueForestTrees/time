package time.dumper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import time.dumper.crawler.IPageHandler;

@Component
public class Helper{
    
    @Autowired 
    private static IPageHandler pageHandler;
       
    public static IPageHandler getPageHandler(){
        return pageHandler;
    }
    
}
