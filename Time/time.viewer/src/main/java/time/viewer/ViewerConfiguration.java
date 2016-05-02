package time.viewer;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

public class ViewerConfiguration extends Configuration {
    @NotEmpty
    private String homeDir;

    @JsonProperty
    public String getHomeDir() {
        return homeDir;
    }

    @JsonProperty
    public void setHomeDir(String name) {
        this.homeDir = name;
    }
}