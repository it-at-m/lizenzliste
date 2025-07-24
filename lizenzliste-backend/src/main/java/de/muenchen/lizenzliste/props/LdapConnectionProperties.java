package de.muenchen.lizenzliste.props;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties
@SuppressFBWarnings(value = { "EI_EXPOSE_REP" }, justification = "Is ok for me")
public class LdapConnectionProperties {
    @NotBlank private String url;
    private int port;
    private int kdcPort;
    private String kdcRealm;
    private String baseDn;
    private String userDn;
    private String password;
    private List<String> userOus;
}
