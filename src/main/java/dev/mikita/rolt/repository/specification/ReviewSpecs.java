package dev.mikita.rolt.repository.specification;

import dev.mikita.rolt.model.Consumer;
import dev.mikita.rolt.model.Contract;
import dev.mikita.rolt.model.PublicationStatus;
import dev.mikita.rolt.model.Review;
import org.springframework.data.jpa.domain.Specification;

public class ReviewSpecs {
    private ReviewSpecs() {
    }

    public static Specification<Review> withStatus(PublicationStatus status) {
        return (root, query, cb) ->
                cb.equal(root.get("status"), status);
    }

    public static Specification<Review> withAuthor(Consumer author) {
        return (root, query, cb) ->
                cb.equal(root.get("author"), author);
    }

    public static Specification<Review> withContract(Contract contract) {
        return (root, query, cb) ->
                cb.equal(root.get("contract"), contract);
    }
}
