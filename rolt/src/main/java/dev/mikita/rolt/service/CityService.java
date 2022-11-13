package dev.mikita.rolt.service;

import dev.mikita.rolt.dao.CityDao;
import dev.mikita.rolt.entity.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CityService {
    private final CityDao dao;

    @Autowired
    public CityService(CityDao dao) {
        this.dao = dao;
    }

    @Transactional(readOnly = true)
    public List<City> findAll() {
        return dao.findAll();
    }

    @Transactional(readOnly = true)
    public City find(Integer id) {
        return dao.find(id);
    }

    @Transactional
    public void persist(City city) {
        dao.persist(city);
    }

    @Transactional
    public void update(City city) {
        dao.update(city);
    }

    @Transactional
    public void remove(City city) {
        dao.remove(city);
    }
}
