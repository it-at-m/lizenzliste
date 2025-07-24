package de.muenchen.lizenzliste.configurations;

import de.muenchen.lizenzliste.controller.LizenzListeController;
import de.muenchen.lizenzliste.dto.SoftwareGroups;
import de.muenchen.lizenzliste.props.LizenzListeProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertyConfiguration {
    @Bean
    public SoftwareGroups extractSoftwareGroups(LizenzListeProperties properties) {
        return properties.getGroups();
    }
}
