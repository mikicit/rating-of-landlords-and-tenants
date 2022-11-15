package dev.mikita.rolt.service;

import dev.mikita.rolt.dao.ContractDao;
import dev.mikita.rolt.entity.*;
import dev.mikita.rolt.exception.IncorrectDateRangeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

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
    public List<Contract> findByProperty(Property property) {
        return dao.findByProperty(property);
    }

    @Transactional(readOnly = true)
    public List<Contract> findByUser(User user) {
        return dao.findByUser(user);
    }

    @Transactional(readOnly = true)
    public Contract find(Integer id) {
        return dao.find(id);
    }

    @Transactional
    public void persist(Contract contract) {
        List<Contract> intersections = dao.findIntersectionsByDateRange(contract.getProperty(), contract.getStartDate(), contract.getEndDate());

        if (intersections.size() > 0) {
            throw new IncorrectDateRangeException("Contracts already exist in this date range.");
        }

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
