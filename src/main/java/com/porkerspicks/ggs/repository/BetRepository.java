package com.porkerspicks.ggs.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.porkerspicks.ggs.domain.Bet;

@Repository
public interface BetRepository extends PagingAndSortingRepository<Bet, String> {

	Page<Bet> findAll(Pageable pageable);
}
