package com.porkerspicks.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.porkerspicks.domain.Pick;
import com.porkerspicks.repository.PickRepository;

@Controller
@RequestMapping("/")
public class PickController {
	
	private PickRepository pickRepository;
	
	public PickController( PickRepository pickRepository ) {
		this.pickRepository = pickRepository;
	}
	
	@RequestMapping(value="/{division}",method=RequestMethod.GET)
	public String divisionsPicks( @PathVariable("division") String division, Model model ) {
		List<Pick> divisionsPicks = pickRepository.findByDivisioin(division);
		model.addAllAttributes(divisionsPicks);
		return "pickList";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String addPick( Pick pick ) {
		pickRepository.save(pick);
		return "redirect:/pickList";
	}	
}