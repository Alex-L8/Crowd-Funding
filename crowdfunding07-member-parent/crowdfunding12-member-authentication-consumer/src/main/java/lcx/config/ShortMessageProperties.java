package lcx.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Create by LCX on 3/1/2022 11:28 PM
 */
@Configuration
@ConfigurationProperties(prefix = "short.message")
public class ShortMessageProperties {
    private String host;
    private String path;
    private String method;
    private String appCode;
    private String template_id;

    public ShortMessageProperties() {
    }

    public ShortMessageProperties(String host, String path, String method, String appCode, String template_id) {
        this.host = host;
        this.path = path;
        this.method = method;
        this.appCode = appCode;
        this.template_id = template_id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    @Override
    public String toString() {
        return "ShortMessageProperties{" +
                "host='" + host + '\'' +
                ", path='" + path + '\'' +
                ", method='" + method + '\'' +
                ", appCode='" + appCode + '\'' +
                ", template_id='" + template_id + '\'' +
                '}';
    }
}
