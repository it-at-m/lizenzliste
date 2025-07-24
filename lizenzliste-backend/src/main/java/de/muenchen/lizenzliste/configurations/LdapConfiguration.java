package de.muenchen.lizenzliste.configurations;

import de.muenchen.lizenzliste.props.LizenzListeProperties;
import de.muenchen.lizenzliste.service.LdapService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LdapConfiguration {
    @Bean
    public LdapService createLdapService(final LizenzListeProperties properties) throws Exception {
        return new LdapService(properties.getAd());
    }
}
