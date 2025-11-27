package com.cars_management.Repository;

import com.cars_management.Controller.Contracts.Contract;
import java.util.List;

public interface IContractRepository {

    void save(Contract contract);

    void update(Contract contract);

    void delete(Integer id);

    List<Contract> findAll();

}