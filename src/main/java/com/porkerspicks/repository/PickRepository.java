package com.porkerspicks.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.porkerspicks.domain.Pick;

public interface PickRepository extends JpaRepository<Pick, Integer> {
	
	List<Pick> findByDivision( String division );	
}
