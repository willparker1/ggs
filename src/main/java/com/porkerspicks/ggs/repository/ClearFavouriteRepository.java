package com.porkerspicks.ggs.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.porkerspicks.ggs.domain.CandidateBet;

@Repository
public interface ClearFavouriteRepository extends CrudRepository<CandidateBet, String> {

}
