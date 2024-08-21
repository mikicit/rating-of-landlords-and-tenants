package dev.mikita.rolt.repository.specification;

import dev.mikita.rolt.model.City;
import dev.mikita.rolt.model.Property;
import dev.mikita.rolt.model.PropertyType;
import dev.mikita.rolt.model.Property_;
import org.springframework.data.jpa.domain.Specification;

public class PropertySpecs {
    private PropertySpecs() {
    }

    public static Specification<Property> withCity(City city) {
        return (root, query, cb) ->
                cb.equal(root.get(Property_.city), city);
    }

    public static Specification<Property> withPropertyType(PropertyType type) {
        return (root, query, cb) ->
                cb.equal(root.get(Property_.type), type);
    }

    public static Specification<Property> withMinSquare(Double minSquare) {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get(Property_.square), minSquare);
    }

    public static Specification<Property> withMaxSquare(Double maxSquare) {
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get(Property_.square), maxSquare);
    }

    public static Specification<Property> withAvailability(Boolean available) {
        return (root, query, cb) ->
                cb.equal(root.get(Property_.isAvailable), available);
    }
}
