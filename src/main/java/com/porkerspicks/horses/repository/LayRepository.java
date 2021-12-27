package com.porkerspicks.horses.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import com.porkerspicks.horses.domain.Lay;

@Repository
public interface LayRepository extends CrudRepository<Lay, String> {

}
