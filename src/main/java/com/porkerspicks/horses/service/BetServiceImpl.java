package com.porkerspicks.horses.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.porkerspicks.horses.domain.Bet;
import com.porkerspicks.horses.exceptions.HTTP400Exception;
import com.porkerspicks.horses.repository.BetRepository;

@Service
public class BetServiceImpl implements BetService {
	final static Logger logger = LoggerFactory.getLogger(BetServiceImpl.class);
	@Autowired
	private BetRepository betRepository;

	@Override
	public Bet save(Bet bet) {
		try {
			return betRepository.save(bet);
		} catch (Exception e) {
			throw new HTTP400Exception(e.getMessage());
		}
	}

	@Override
	public Optional<Bet> get(String id) {
		return betRepository.findById(id);
	}

	@Override
	public Page<Bet> getBetsByPage(Integer pageNumber, Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("betId").descending());
		return betRepository.findAll(pageable);
	}

	@Override
	public void update(String id, Bet Bet) {
		betRepository.save(Bet);
	}

	@Override
	public void delete(String id) {
		betRepository.deleteById(id);
	}
}
