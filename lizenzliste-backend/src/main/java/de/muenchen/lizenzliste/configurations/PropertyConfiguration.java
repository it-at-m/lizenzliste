package de.muenchen.lizenzliste.configurations;

import de.muenchen.lizenzliste.dto.SoftwareGroup;
import de.muenchen.lizenzliste.props.LizenzListeProperties;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertyConfiguration {
    @Bean
    public Map<String, SoftwareGroup> extractSoftwareGroups(final LizenzListeProperties properties) {
        return properties.getGroups();
    }
}
