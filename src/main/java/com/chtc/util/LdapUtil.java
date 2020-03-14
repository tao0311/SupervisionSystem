package com.chtc.util;

import javax.naming.*;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.Properties;

/**
 * Created by Xu Qingxin on 2016-10-20.
 */
public class LdapUtil {

    public static boolean isValidUser(String ip, int port, String base, String domain, String username, String password) {
        boolean result = true;
        Properties env = new Properties();
        String ldapURL = String.format("LDAP://%s:%d", ip, port);
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");//"none","simple","strong"
        env.put(Context.SECURITY_PRINCIPAL, username.indexOf(domain) < 0 ? username + "@" + domain : username);
        env.put(Context.SECURITY_CREDENTIALS, password);
        env.put(Context.PROVIDER_URL, ldapURL);
        try {
            LdapContext ctx = new InitialLdapContext(env, null);
            SearchControls searchCtls = new SearchControls();
            searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            String searchFilter = "(&(objectCategory=person)(objectClass=user)(name=*))";
            String searchBase = base;
            String returnedAtts[] = {"memberOf"};
            searchCtls.setReturningAttributes(returnedAtts);
            NamingEnumeration<SearchResult> answer = ctx.search(searchBase, searchFilter, searchCtls);
            ctx.close();
        } catch (AuthenticationException e) {
            e.printStackTrace();
            result = false;
        } catch (InvalidNameException e) {
            e.printStackTrace();
            result = false;
        } catch (CommunicationException e) {
            e.printStackTrace();
            result = false;
        } catch (NamingException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }
}
