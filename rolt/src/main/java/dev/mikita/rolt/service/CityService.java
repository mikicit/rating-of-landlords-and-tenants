package dev.mikita.rolt.service;

import dev.mikita.rolt.dao.CityDao;
import dev.mikita.rolt.entity.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CityService {
    private final CityDao cityDao;

    @Autowired
    public CityService(CityDao cityDao) {
        this.cityDao = cityDao;
    }

    @Transactional(readOnly = true)
    public Page<City> findAll(Pageable pageable, String name) {
        return cityDao.findAll(pageable, name);
    }

    @Transactional(readOnly = true)
    public City find(Integer id) {
        return cityDao.find(id);
    }

    @Transactional
    public void persist(City city) {
        cityDao.persist(city);
    }

    @Transactional
    public void update(City city) {
        cityDao.update(city);
    }

    @Transactional
    public void remove(City city) {
        cityDao.remove(city);
    }
}
