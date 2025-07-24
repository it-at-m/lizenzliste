package de.muenchen.lizenzliste.service;

import de.muenchen.lizenzliste.props.LdapConnectionProperties;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.ldap.client.api.DefaultLdapConnectionFactory;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;
import org.apache.directory.ldap.client.api.SaslGssApiRequest;

public class CustomLdapConnectionFactory extends DefaultLdapConnectionFactory {

    private final LdapConnectionProperties ldapProperties;

    public CustomLdapConnectionFactory(final LdapConnectionProperties properties) {
        super(configBuilder(properties));
        this.ldapProperties = properties;
    }

    @Override
    public LdapConnection bindConnection(final LdapConnection connection) throws LdapException {
        final SaslGssApiRequest request = new SaslGssApiRequest();
        request.setMutualAuthentication(true);
        request.setKdcHost(this.ldapProperties.getUrl());
        request.setKdcPort(ldapProperties.getKdcPort());
        request.setRealmName(ldapProperties.getKdcRealm());

        connection.bind(request);
        return connection;
    }

    private static LdapConnectionConfig configBuilder(final LdapConnectionProperties properties) {
        final LdapConnectionConfig config = new LdapConnectionConfig();
        config.setLdapHost(properties.getUrl());
        config.setCredentials(properties.getPassword());
        config.setLdapPort(properties.getPort());
        config.setUseSsl(false);
        config.setUseTls(false);
        config.setName(properties.getUserDn());
        config.setTimeout(60 * 1000L);
        return config;
    }
}
