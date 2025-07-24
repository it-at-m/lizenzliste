package de.muenchen.lizenzliste.service;

import com.sun.security.auth.module.Krb5LoginModule;
import de.muenchen.lizenzliste.props.LizenzListeProperties;
import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomKrbLoginModule extends Krb5LoginModule {

    private static String user;
    private static char[] password;

    @Bean
    public static Void configureAdCredentials(final LizenzListeProperties properties) {
        user = properties.getAd().getUserDn();
        password = properties.getAd().getPassword().toCharArray();
        System.setProperty("java.security.krb5.realm", properties.getAd().getKdcRealm());
        System.setProperty("java.security.krb5.kdc", properties.getAd().getUrl());
        return null;
    }

    @Override
    public void initialize(final Subject subject,
            final CallbackHandler callbackHandler,
            final Map<String, ?> sharedState,
            final Map<String, ?> options) {
        super.initialize(subject, callbackHandler, sharedState, options);

        ((Map<String, Object>) sharedState).put("javax.security.auth.login.name", user);
        ((Map<String, Object>) sharedState).put("javax.security.auth.login.password", password);
    }
}
