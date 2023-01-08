package dev.mikita.rolt.security.model;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import java.security.Principal;
import java.util.Collection;

public class AuthenticationToken extends AbstractAuthenticationToken implements Principal {
    private CustomUserDetails userDetails;

    public AuthenticationToken(Collection<? extends GrantedAuthority> authorities, CustomUserDetails userDetails) {
        super(authorities);
        this.userDetails = userDetails;
        super.setAuthenticated(true);
        super.setDetails(userDetails);
    }

    @Override
    public String getCredentials() {
        return userDetails.getPassword();
    }

    @Override
    public CustomUserDetails getPrincipal() {
        return userDetails;
    }
}
