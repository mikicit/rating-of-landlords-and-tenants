package dev.mikita.rolt.service;

import dev.mikita.rolt.dao.ReviewDao;
import dev.mikita.rolt.entity.PublicationStatus;
import dev.mikita.rolt.entity.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.Objects;

@Service
public class ReviewService {
    private final ReviewDao reviewDao;

    @Autowired
    public ReviewService(ReviewDao reviewDao) {
        this.reviewDao = reviewDao;
    }

    @Transactional(readOnly = true)
    public Page<Review> findAll(Pageable pageable, Map<String, Object> filters) {
        return reviewDao.findAll(pageable, filters);
    }

    @Transactional(readOnly = true)
    public Review find(Integer id) {
        return reviewDao.find(id);
    }

    @Transactional
    public void persist(Review review) {
        reviewDao.persist(review);
    }

    @Transactional
    public void update(Review review) {
        Objects.requireNonNull(review);
        reviewDao.update(review);
    }

    @Transactional
    public void remove(Review review) {
        Objects.requireNonNull(review);
        review.setStatus(PublicationStatus.DELETED);
        reviewDao.update(review);
    }

    @Transactional
    public void publish(Review review) {
        Objects.requireNonNull(review);
        review.setStatus(PublicationStatus.PUBLISHED);
        reviewDao.update(review);
    }

    @Transactional
    public void moderate(Review review) {
        Objects.requireNonNull(review);
        review.setStatus(PublicationStatus.MODERATION);
        reviewDao.update(review);
    }
}
