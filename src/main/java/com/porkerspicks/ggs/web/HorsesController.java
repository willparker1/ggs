package com.porkerspicks.ggs.web;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.porkerspicks.ggs.domain.Bet;
import com.porkerspicks.ggs.service.events.BetEvent;
import com.porkerspicks.ggs.service.BetService;

@RestController
@RequestMapping("/")
public class HorsesController extends AbstractController {

	private BetService betService;

	public HorsesController(BetService betService) {
		this.betService = betService;
	}

	@PostMapping("/bet")
	public ResponseEntity<?> addBet(@RequestBody Bet bet) {
		Bet savedBet = betService.save(bet);
		BetEvent betEvent = new BetEvent("One Bet is created", savedBet);
		eventPublisher.publishEvent(betEvent);
		return ResponseEntity.ok().body("New Bet has been saved with ID:" + savedBet.getBetId());
	}

	@GetMapping("/bet/{id}")
	// @ResponseBody
	public Bet getBet(@PathVariable("id") String id) {
		Optional<Bet> returnedBet = betService.get(id);
		Bet bet = returnedBet.get();
		BetEvent betEvent = new BetEvent("One bet is retrieved", bet);
		eventPublisher.publishEvent(betEvent);
		return bet;
	}

	@GetMapping("/bet")
	// @ResponseBody
	public Page<Bet> getBetsByPage(
			@RequestParam(value = "pagenumber", required = true, defaultValue = "0") Integer pageNumber,
			@RequestParam(value = "pagesize", required = true, defaultValue = "20") Integer pageSize) {
		Page<Bet> pagedBets = betService.getBetsByPage(pageNumber, pageSize);
		return pagedBets;
	}

	@PutMapping("/bet/{id}")
	public ResponseEntity<?> updateBet(@PathVariable("id") String id, @RequestBody Bet bet) {
		checkResourceFound(this.betService.get(id));
		betService.update(id, bet);
		return ResponseEntity.ok().body("Bet has been updated successfully.");
	}

	@DeleteMapping("/bet/{id}")
	public ResponseEntity<?> deleteBet(@PathVariable("id") String id) {
		checkResourceFound(this.betService.get(id));
		betService.delete(id);
		return ResponseEntity.ok().body("Bet has been deleted successfully.");
	}
}