package de.muenchen.lizenzliste.props;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties
public class LdapConnectionProperties {
    @NotBlank
    private String url;
    private int port;
    private int kdcPort;
    private String kdcRealm;
    private String baseDn;
    private String userDn;
    private String password;
    private List<String> userOus;
}
