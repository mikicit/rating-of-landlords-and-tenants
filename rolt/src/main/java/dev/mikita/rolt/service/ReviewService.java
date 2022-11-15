package dev.mikita.rolt.service;

import dev.mikita.rolt.dao.ReviewDao;
import dev.mikita.rolt.entity.PublicationStatus;
import dev.mikita.rolt.entity.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ReviewService {
    private final ReviewDao dao;

    @Autowired
    public ReviewService(ReviewDao dao) {
        this.dao = dao;
    }

    @Transactional(readOnly = true)
    public List<Review> findAll() {
        return dao.findAll();
    }

    @Transactional(readOnly = true)
    public Review find(Integer id) {
        return dao.find(id);
    }

    @Transactional
    public void persist(Review review) {
        dao.persist(review);
    }

    @Transactional
    public void update(Review review) {
        Objects.requireNonNull(review);
        dao.update(review);
    }

    @Transactional
    public void remove(Review review) {
        Objects.requireNonNull(review);
        review.setStatus(PublicationStatus.DELETED);
        dao.update(review);
    }

    @Transactional
    public void publish(Review review) {
        Objects.requireNonNull(review);
        review.setStatus(PublicationStatus.PUBLISHED);
        dao.update(review);
    }

    @Transactional
    public void moderate(Review review) {
        Objects.requireNonNull(review);
        review.setStatus(PublicationStatus.MODERATION);
        dao.update(review);
    }
}
