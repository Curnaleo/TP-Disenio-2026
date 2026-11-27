package com.grupo30.capitalhumano.evaluaciones.autenticacion.ldap;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

/**
 * Componente encargado de validar credenciales de consultores contra el servidor LDAP
 * de la organización (CU001 - Paso 5).
 *
 * <p>Cuando {@code app.ldap.enabled=true}, realiza una autenticación real mediante JNDI
 * ({@link InitialDirContext}). Cuando {@code app.ldap.enabled=false} (modo desarrollo/pruebas),
 * valida contra la contraseña de desarrollo configurada.</p>
 */
@Slf4j
@Component
public class LdapAuthService {

    @Value("${app.ldap.enabled:false}")
    private boolean ldapEnabled;

    @Value("${app.ldap.url:ldap://localhost:389}")
    private String ldapUrl;

    @Value("${app.ldap.base-dn:dc=capitalhumano,dc=com}")
    private String baseDn;

        @Value("${app.ldap.dev-password:admin123}")
    private String devPassword;

    /**
     * Valida usuario y contraseña en LDAP.
     *
     * @param username identificador en mayúsculas (máx. 15 caracteres).
     * @param password contraseña del usuario (máx. 20 caracteres).
     * @return {@code true} si las credenciales son válidas.
     */

    public boolean autenticar(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return false;
        }
        if (!ldapEnabled) {
            log.debug("LDAP deshabilitado: validando usuario {} en modo desarrollo.", username);
            return devPassword.equals(password);
        }
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, ldapUrl);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, "uid=" + username + "," + baseDn);
        env.put(Context.SECURITY_CREDENTIALS, password);
        InitialDirContext ctx = null;
        try {
            ctx = new InitialDirContext(env);
            return true;
        } catch (NamingException ex) {
            log.warn("Fallo de autenticación LDAP para usuario {}: {}", username, ex.getMessage());
            return false;
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (NamingException ignored) {
                }
            }
        }
    }
}
