package de.muenchen.lizenzliste.service;

import de.muenchen.lizenzliste.dto.Person;
import de.muenchen.lizenzliste.dto.SoftwareGroup;
import de.muenchen.lizenzliste.props.LdapConnectionProperties;
import org.apache.directory.api.ldap.model.cursor.CursorException;
import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.ldap.client.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.apache.directory.ldap.client.api.search.FilterBuilder.*;

public class LdapService {

    private final Logger LOG = LoggerFactory.getLogger(LdapService.class);

    private final LdapConnectionProperties ad;

    private LdapConnectionPool ldapPool;

    public LdapService(LdapConnectionProperties adProperties) throws Exception {
        this.ad = adProperties;
        this.setupLdapPool();
    }

    public String getDepartment(Person p) throws LdapException, IOException {
        LdapConnection conn = null;
        String department = null;
        try {
            conn = ldapPool.getConnection();
            var q = and(
                    equal("uid", p.getUid()),
                    equal("objectClass", "person")
            );
            for (var ou : ad.getUserOus()) {
                try (EntryCursor cursor = conn.search(ou, q.toString(), SearchScope.SUBTREE)) {
                    if (!cursor.next())
                    {
                        continue;
                    }
                    var entry = cursor.get();
                    department = entry.get("department").get().toString();
                    break;
                } catch (CursorException e)
                {
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

    public List<Person> getMembersOfGroupFilteredByDepartment(String group, String department) throws LdapException, IOException {
        LdapConnection conn = null;
        List<Person> users = new ArrayList<>();
        try {
            conn = ldapPool.getConnection();
            var q = and(
                    equal("objectCategory", "user"),
                    equal("memberOf", group),
                    startsWith("department", department)
            );
            LOG.info("Searching for query '{}'", q);
            try (EntryCursor cursor = conn.search(this.ad.getBaseDn(), q.toString(), SearchScope.SUBTREE)) {
                cursor.getSearchResultDone();
                cursor.forEach((e) -> {
                    var uid = e.get("uid").get().toString();
                    var d = e.get("department").get().toString();
                    var displayName = e.get("displayName").get().toString();
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

    private void setupLdapPool() throws Exception {
        var pool = new LdapConnectionPool(new DefaultPoolableLdapConnectionFactory(new CustomLdapConnectionFactory(this.ad)));

        Configuration.setConfiguration(new Configuration() {
            @Override
            public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
                return new AppConfigurationEntry[]{
                        new AppConfigurationEntry(
                                CustomKrbLoginModule.class.getName(),
                                AppConfigurationEntry.LoginModuleControlFlag.REQUIRED,
                                Map.of("useFirstPass", "true")
                        )
                };
            }
        });

        this.ldapPool = pool;
    }

    public List<Person> getADGroupMembers(SoftwareGroup group, Person p) throws IOException {
        List<Person> result = new ArrayList<>();
        try {
            var department = getDepartment(p);

            result = getMembersOfGroupFilteredByDepartment(group.getGroup(), department);
        } catch (LdapException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}
