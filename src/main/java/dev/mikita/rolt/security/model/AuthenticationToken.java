package dev.mikita.rolt.security.model;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import java.security.Principal;
import java.util.Collection;
import java.util.Objects;

/**
 * The type Authentication token.
 */
public class AuthenticationToken extends AbstractAuthenticationToken implements Principal {
    private final CustomUserDetails userDetails;

    /**
     * Instantiates a new Authentication token.
     *
     * @param authorities the authorities
     * @param userDetails the user details
     */
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthenticationToken that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(userDetails, that.userDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userDetails);
    }
}
