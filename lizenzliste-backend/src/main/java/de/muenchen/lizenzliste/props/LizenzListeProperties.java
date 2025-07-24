package de.muenchen.lizenzliste.props;

import de.muenchen.lizenzliste.dto.SoftwareGroup;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "lizenzliste")
@AllArgsConstructor
@NoArgsConstructor
@SuppressFBWarnings(value = { "EI_EXPOSE_REP" }, justification = "Is ok for me")
public class LizenzListeProperties {

    @NestedConfigurationProperty
    @Valid private LdapConnectionProperties ad;

    @NestedConfigurationProperty
    private Map<String, SoftwareGroup> groups;
}
