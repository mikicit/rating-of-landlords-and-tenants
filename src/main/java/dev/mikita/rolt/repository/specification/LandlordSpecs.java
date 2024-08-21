package dev.mikita.rolt.repository.specification;

import dev.mikita.rolt.model.ConsumerGender;
import dev.mikita.rolt.model.ConsumerStatus;
import dev.mikita.rolt.model.Landlord;
import dev.mikita.rolt.model.Landlord_;
import org.springframework.data.jpa.domain.Specification;

public class LandlordSpecs {
    private LandlordSpecs() {
    }

    public static Specification<Landlord> withGender(ConsumerGender gender) {
        return (root, query, cb) ->
                cb.equal(root.get(Landlord_.gender), gender);
    }

    public static Specification<Landlord> withStatus(ConsumerStatus status) {
        return (root, query, cb) ->
                cb.equal(root.get(Landlord_.status), status);
    }
}
