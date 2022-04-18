package com.porkerspicks.ggs.service;

import java.util.Optional;
import org.springframework.data.domain.Page;

import com.porkerspicks.ggs.domain.Bet;

public interface BetService {
	
	Bet save(Bet category);

	Optional<Bet> get(String id);

	Page<Bet> getBetsByPage(Integer pageNumber, Integer pageSize);

	void update(String id, Bet category);

	void delete(String id);
}
