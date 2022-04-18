package com.porkerspicks.ggs.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import com.porkerspicks.ggs.domain.Lay;

@Repository
public interface LayRepository extends CrudRepository<Lay, String> {

}
