package dev.mikita.rolt.repository.specification;

import dev.mikita.rolt.model.*;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDate;

public class ContractSpecs {
    private ContractSpecs() {
    }

    public static Specification<Contract> withLandlord(Landlord landlord) {
        return (root, query, cb) ->
                cb.equal(root.get(Contract_.property).get(Property_.owner), landlord);
    }

    public static Specification<Contract> withTenant(Tenant tenant) {
        return (root, query, cb) ->
                cb.equal(root.get(Contract_.tenant), tenant);
    }

    public static Specification<Contract> withProperty(Property property) {
        return (root, query, cb) ->
                cb.equal(root.get(Contract_.property), property);
    }

    public static Specification<Contract> withStartDateAfter(LocalDate fromDate) {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get(Contract_.startDate), fromDate);
    }

    public static Specification<Contract> withEndDateBefore(LocalDate toDate) {
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get(Contract_.endDate), toDate);
    }
}
