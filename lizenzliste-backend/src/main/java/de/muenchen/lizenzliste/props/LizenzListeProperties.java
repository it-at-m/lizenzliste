package de.muenchen.lizenzliste.props;

import de.muenchen.lizenzliste.dto.SoftwareGroups;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
public class LizenzListeProperties {

    @NestedConfigurationProperty
    @Valid
    LdapConnectionProperties ad;

    @NestedConfigurationProperty
    SoftwareGroups groups;
}
