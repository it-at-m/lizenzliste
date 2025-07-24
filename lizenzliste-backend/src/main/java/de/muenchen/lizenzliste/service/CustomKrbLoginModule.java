package de.muenchen.lizenzliste.service;

import com.sun.security.auth.module.Krb5LoginModule;
import de.muenchen.lizenzliste.props.LizenzListeProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import java.util.Map;

@Configuration
public class CustomKrbLoginModule extends Krb5LoginModule {

    private static String user;
    private static char[] password;

    @Bean
    public static Void configureAdCredentials(LizenzListeProperties properties)
    {
        user = properties.getAd().getUserDn();
        password = properties.getAd().getPassword().toCharArray();
        return null;
    }

    public void initialize(Subject subject,
                           CallbackHandler callbackHandler,
                           Map<String, ?> sharedState,
                           Map<String, ?> options)
    {
        super.initialize(subject, callbackHandler, sharedState, options);

        ((Map<String, Object>)sharedState).put("javax.security.auth.login.name", user);
        ((Map<String, Object>)sharedState).put("javax.security.auth.login.password", password);
    }
}
