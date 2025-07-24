package de.muenchen.lizenzliste.service;

import static org.apache.directory.ldap.client.api.search.FilterBuilder.and;
import static org.apache.directory.ldap.client.api.search.FilterBuilder.equal;
import static org.apache.directory.ldap.client.api.search.FilterBuilder.startsWith;

import de.muenchen.lizenzliste.dto.Person;
import de.muenchen.lizenzliste.dto.SoftwareGroup;
import de.muenchen.lizenzliste.props.LdapConnectionProperties;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import org.apache.directory.api.ldap.model.cursor.CursorException;
import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.ldap.client.api.DefaultPoolableLdapConnectionFactory;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapConnectionPool;
import org.apache.directory.ldap.client.api.search.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LdapService {

    private final Logger log = LoggerFactory.getLogger(LdapService.class);

    private final LdapConnectionProperties ad;

    private final LdapConnectionPool ldapPool;

    public LdapService(final LdapConnectionProperties adProperties) {
        this.ad = adProperties;
        this.ldapPool = this.setupLdapPool();
    }

    public String getDepartment(final Person p) throws LdapException, IOException {
        LdapConnection conn = null;
        String department = null;
        try {
            conn = ldapPool.getConnection();
            final FilterBuilder q = and(
                    equal("uid", p.getUid()),
                    equal("objectClass", "person"));
            for (final String ou : ad.getUserOus()) {
                try (EntryCursor cursor = conn.search(ou, q.toString(), SearchScope.SUBTREE)) {
                    if (!cursor.next()) {
                        continue;
                    }
                    final Entry entry = cursor.get();
                    department = entry.get("department").get().toString();
                    break;
                } catch (CursorException e) {
                    throw new RuntimeException(e);
                }
            }
            if (department == null) {
                throw new RuntimeException("Failed to find user");
            }

        } finally {
            if (conn != null) {
                ldapPool.releaseConnection(conn);
            }
        }
        return department;
    }

    public List<Person> getMembersOfGroupFilteredByDepartment(final String group, final String department) throws LdapException, IOException {
        LdapConnection conn = null;
        final List<Person> users = new ArrayList<>();
        try {
            conn = ldapPool.getConnection();
            final FilterBuilder q = and(
                    equal("objectCategory", "user"),
                    equal("memberOf", group),
                    startsWith("department", department));
            log.info("Searching for query '{}'", q);
            try (EntryCursor cursor = conn.search(this.ad.getBaseDn(), q.toString(), SearchScope.SUBTREE)) {
                cursor.getSearchResultDone();
                cursor.forEach((e) -> {
                    final String uid = e.get("uid").get().toString();
                    final String d = e.get("department").get().toString();
                    final String displayName = e.get("displayName").get().toString();
                    users.add(new Person(uid, displayName, d));
                });
            }
        } finally {
            if (conn != null) {
                ldapPool.releaseConnection(conn);
            }
        }
        return users;
    }

    private LdapConnectionPool setupLdapPool() {
        final LdapConnectionPool pool = new LdapConnectionPool(new DefaultPoolableLdapConnectionFactory(new CustomLdapConnectionFactory(this.ad)));

        Configuration.setConfiguration(new Configuration() {
            @Override
            public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
                return new AppConfigurationEntry[] {
                        new AppConfigurationEntry(
                                CustomKrbLoginModule.class.getName(),
                                AppConfigurationEntry.LoginModuleControlFlag.REQUIRED,
                                Map.of("useFirstPass", "true"))
                };
            }
        });

        return pool;
    }

    public List<Person> getADGroupMembers(final SoftwareGroup group, final Person p) throws IOException {
        final List<Person> result;
        try {
            final String department = getDepartment(p);

            result = getMembersOfGroupFilteredByDepartment(group.getGroup(), department);
        } catch (LdapException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}
