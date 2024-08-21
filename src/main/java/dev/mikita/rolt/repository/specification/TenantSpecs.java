package dev.mikita.rolt.repository.specification;

import dev.mikita.rolt.model.ConsumerGender;
import dev.mikita.rolt.model.ConsumerStatus;
import dev.mikita.rolt.model.Tenant;
import org.springframework.data.jpa.domain.Specification;

public class TenantSpecs {
    private TenantSpecs() {
    }

    public static Specification<Tenant> withStatus(ConsumerStatus status) {
        return (root, query, cb) ->
                cb.equal(root.get("status"), status);
    }

    public static Specification<Tenant> withGender(ConsumerGender gender) {
        return (root, query, cb) ->
                cb.equal(root.get("gender"), gender);
    }
}
