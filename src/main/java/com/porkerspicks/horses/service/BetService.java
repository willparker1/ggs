package com.porkerspicks.horses.service;

import java.util.Optional;
import org.springframework.data.domain.Page;

import com.porkerspicks.horses.domain.Bet;

public interface BetService {
	
	Bet save(Bet category);

	Optional<Bet> get(String id);

	Page<Bet> getBetsByPage(Integer pageNumber, Integer pageSize);

	void update(String id, Bet category);

	void delete(String id);
}
