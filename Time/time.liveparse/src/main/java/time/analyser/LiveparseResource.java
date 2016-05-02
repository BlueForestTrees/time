package time.analyser;

import com.codahale.metrics.annotation.Timed;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/view")
@Produces(MediaType.APPLICATION_JSON)
public class LiveparseResource {

    public LiveparseResource() {

    }

    @GET
    @Timed
    public String sayHello() {
        return "hello";
    }
}