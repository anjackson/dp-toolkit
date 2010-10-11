/**
 * 
 */
package net.lovelycode.dp.contentprofiler.metadata;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

/**
 * @author AnJackson
 *
 */
public class User {
    
    String username;
    String userhome;
    String userdir;
    String fullname;
    String email;
    // ??? 
    
    public User() {
        this.username = System.getProperty("user.name");
        this.userhome = System.getProperty("user.home");
        this.userdir = System.getProperty("user.dir");
    }

    public static void ldapTest()
    {
        Hashtable env= new Hashtable(11);
        env.put(Context.SECURITY_AUTHENTICATION,"none");
        //      env.put(Context.SECURITY_PRINCIPAL,"CN=kiran,OU=LinkedgeOU,DC=LINKEDGEDOMAIN");//User
        //      env.put(Context.SECURITY_CREDENTIALS, "kiran");//Password
        //env.put(Context.SECURITY_PRINCIPAL,"CN=AnJackson,DC=AD");//User
        //env.put(Context.SECURITY_CREDENTIALS, "");//Password
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        //        env.put(Context.PROVIDER_URL,"ldap://localhost:389/DC=AD");
        env.put(Context.PROVIDER_URL,"ldap://ad.bl.uk:389/");
        try
        {
            DirContext ctx = new InitialDirContext(env);
            String[] sAttrIDs = new String[2];

            Attributes attr = ctx.getAttributes("");
            NamingEnumeration<String> namingEnumeration = attr.getIDs();
            while( namingEnumeration.hasMore() ) {
                String attrID = namingEnumeration.next();
                System.out.println("List:"+attrID+"="+attr.get(attrID));
            }
            //
            /*
            Attributes matchAttrs = new BasicAttributes(true); // ignore attribute name case
            //matchAttrs.put(new BasicAttribute("sn", "Geisel"));
            matchAttrs.put(new BasicAttribute("mail"));
            matchAttrs.put(new BasicAttribute("sn", "Jackson"));
            // Search for objects that have those matching attributes
            NamingEnumeration answer = ctx.search("ou=People", matchAttrs);

            while (answer.hasMore()) {
                SearchResult sr = (SearchResult)answer.next();
                System.out.println(">>>" + sr.getName());
                printAttrs(sr.getAttributes());
            }
             */

        }
        catch(NamingException e)
        {
            System.err.println("Problem getting attribute: " + e);
        }
    }

    /**
     * @param attributes
     * @throws NamingException 
     */
    private static void printAttrs(Attributes attributes) throws NamingException {
        for (NamingEnumeration ae = attributes.getAll(); ae.hasMore();) {
            Attribute attr = (Attribute)ae.next();
            System.out.println("attribute: " + attr.getID());
            /* Print each value */
            for (NamingEnumeration e = attr.getAll(); e.hasMore();
            System.out.println("value: " + e.next()))
                ;
        }

    }
}
