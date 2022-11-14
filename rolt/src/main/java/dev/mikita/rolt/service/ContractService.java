package dev.mikita.rolt.service;

import dev.mikita.rolt.dao.ContractDao;
import dev.mikita.rolt.dao.ReviewDao;
import dev.mikita.rolt.entity.Contract;
import dev.mikita.rolt.entity.PublicationStatus;
import dev.mikita.rolt.entity.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ContractService {
    private final ContractDao dao;

    @Autowired
    public ContractService(ContractDao dao) {
        this.dao = dao;
    }

    @Transactional(readOnly = true)
    public List<Contract> findAll() {
        return dao.findAll();
    }

    @Transactional(readOnly = true)
    public Contract find(Integer id) {
        return dao.find(id);
    }

    @Transactional
    public void persist(Contract contract) {
        dao.persist(contract);
    }

    @Transactional
    public void update(Contract contract) {
        dao.update(contract);
    }

    @Transactional
    public void remove(Contract contract) {
        dao.remove(contract);
    }
}
