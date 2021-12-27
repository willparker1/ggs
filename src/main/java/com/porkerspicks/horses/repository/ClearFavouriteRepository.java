package com.porkerspicks.horses.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.porkerspicks.horses.domain.Bet;
import com.porkerspicks.horses.domain.CandidateBet;

@Repository
public interface ClearFavouriteRepository extends CrudRepository<CandidateBet, String> {

}
