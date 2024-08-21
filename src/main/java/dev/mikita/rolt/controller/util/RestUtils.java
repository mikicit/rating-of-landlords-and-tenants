package dev.mikita.rolt.controller.util;

import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.Objects;

/**
 * The type Rest utils.
 */
public class RestUtils {
    private RestUtils() {
    }

    public static HttpHeaders createLocationHeaderFromCurrentUri(String path, Object... uriVariableValues) {
        Objects.requireNonNull(path);

        final URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().path(path).buildAndExpand(
                uriVariableValues).toUri();
        final HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.LOCATION, location.toASCIIString());
        return headers;
    }

    public static URI createLocationUriFromCurrentUri(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(id).toUri();
    }
}
