package com.nhamza.keykloak.event.provider;


import java.util.Locale;
import java.util.regex.Pattern;
import org.jboss.logging.Logger;
import org.keycloak.Config.Scope;
import org.keycloak.util.JsonSerialization;


public class TCMConfig {

    private static final Logger log = Logger.getLogger(TCMConfig.class);
    private static final Pattern SPECIAL_CHARACTERS = Pattern.compile("[^*#a-zA-Z0-9 _.-]");
    private static final Pattern SPACE = Pattern.compile(" ");
    private static final Pattern DOT = Pattern.compile("\\.");

   /* @Value("${X-TCM-INTERNAL-KCUPDATE-API-SECRET}")
    private String apiSecret = "configure-nachrichten-api-secret";*/
    private String hostUrl;
    private String endpoint;
    private Integer port;
    private String username;
    private String password;



    public static String writeAsJson(Object object, boolean isPretty) {
        try {
            if (isPretty) {
                return JsonSerialization.writeValueAsPrettyString(object);
            }
            return JsonSerialization.writeValueAsString(object);

        } catch (Exception e) {
            log.error("Could not serialize to JSON", e);
        }
        return "unparsable";
    }


    public static TCMConfig createFromScope(Scope config) {
        TCMConfig cfg = new TCMConfig();

        cfg.hostUrl = resolveConfigVar(config, "url", "localhost");
        cfg.port = Integer.valueOf(resolveConfigVar(config, "port", "8040"));
        cfg.setEndpoint(resolveConfigVar(config, "endpoint", "/v1/kcupdate"));
        cfg.username = resolveConfigVar(config, "username", "admin");
        cfg.password = resolveConfigVar(config, "password", "admin");
        return cfg;

    }

    private static String resolveConfigVar(Scope config, String variableName, String defaultValue) {

        String value = defaultValue;
        if (config != null && config.get(variableName) != null) {
            value = config.get(variableName);
        } else {
            //try from env variables eg: KK_TO_RMQ_URL:
            String envVariableName = "KK_TO_TCM_" + variableName.toUpperCase(Locale.ENGLISH);
            String env = System.getenv(envVariableName);
            if (env != null) {
                value = env;
            }
        }
        if (!"password".equals(variableName)) {
            log.infof("keycloak-to-TCM configuration: %s=%s%n", variableName, value);
        }
        return value;

    }


    public String getHostUrl() {
        return hostUrl;
    }

    public void setHostUrl(String hostUrl) {
        this.hostUrl = hostUrl;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
}
