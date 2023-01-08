package dev.mikita.rolt.service;

import dev.mikita.rolt.dao.ContractDao;
import dev.mikita.rolt.entity.*;
import dev.mikita.rolt.exception.IncorrectDateRangeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Service
public class ContractService {
    private final ContractDao contractDao;

    @Autowired
    public ContractService(ContractDao contractDao) {
        this.contractDao = contractDao;
    }

    @Transactional(readOnly = true)
    public Page<Contract> findAll(Pageable pageable, Map<String, Object> filters) {
        return contractDao.findAll(pageable, filters);
    }

    @Transactional(readOnly = true)
    public Contract find(Integer id) {
        return contractDao.find(id);
    }

    @Transactional
    public void persist(Contract contract) {
        List<Contract> intersections = contractDao.findIntersectionsByDateRange(contract.getProperty(), contract.getStartDate(), contract.getEndDate());

        if (intersections.size() > 0) {
            throw new IncorrectDateRangeException("Contracts already exist in this date range.");
        }

        contractDao.persist(contract);
    }

    @Transactional
    public void update(Contract contract) {
        contractDao.update(contract);
    }

    @Transactional
    public void remove(Contract contract) {
        contractDao.remove(contract);
    }
}
