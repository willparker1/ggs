package com.porkerspicks.ggs;

import com.betfair.aping.api.ApiNgRescriptOperations;
import com.betfair.aping.api.BetfairConfig;
import com.betfair.aping.entities.*;
import com.betfair.aping.enums.*;
import com.porkerspicks.ggs.domain.HorseSelection;
import com.porkerspicks.ggs.domain.Lay;
import com.porkerspicks.ggs.domain.Session;
import com.porkerspicks.ggs.domain.types.LayType;
import com.porkerspicks.ggs.repository.LayRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import javax.mail.MessagingException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This is a demonstration class to show a quick demo of the new Betfair API-NG.
 * When you execute the class will: <li>find a market (next horse race in the
 * UK)</li> <li>get prices and runners on this market</li> <li>place a bet on 1
 * runner</li> <li>handle the error</li>
 * 
 */
@Component
public class FavouriteLayer {

	@Autowired
	private ApiNgRescriptOperations rescriptOperations;
	
	@Autowired
	private BetfairConfig betfairConfig;
	
	@Autowired
	private GMailer gMailer;
	
	@Autowired
	private Authenticator authenticator;
	
	@Autowired
	private LayRepository layRepository;
	
	private Session session;	

	
	//@Scheduled(fixedDelay = 60000)
    public void layBets() {
		
        String applicationKey = betfairConfig.getApplicationKey();

        try {
        	
       		session = authenticator.login();

            MarketFilter marketFilter;
            marketFilter = new MarketFilter();
            Set<String> eventTypeIds = new HashSet<String>();
            eventTypeIds.add("7");

            Date now = new Date();
            System.out.println("Time: " + now);
            
            List<HorseSelection> unfoundSelections = new ArrayList<HorseSelection>();
            
                marketFilter = new MarketFilter();
                marketFilter.setEventTypeIds(eventTypeIds);

                TimeRange time = new TimeRange();
                Date endOfDay = DateUtils.setHours(now, 23);
                time.setFrom( now );
                time.setTo(endOfDay);

                marketFilter.setMarketStartTime( time );
                Set<String> countries = new HashSet<String>();
                countries.add("GB");
                Set<String> typesCode = new HashSet<String>();
                typesCode.add("WIN");
                marketFilter.setMarketCountries(countries);
                marketFilter.setMarketTypeCodes(typesCode);

                Set<MarketProjection> marketProjection = new HashSet<MarketProjection>();
                marketProjection.add(MarketProjection.MARKET_DESCRIPTION);

                List<MarketCatalogue> marketCatalogues = null;
                
               	marketCatalogues = rescriptOperations.listMarketCatalogue(marketFilter, marketProjection, MarketSort.FIRST_TO_START, 
               			applicationKey, session.getSessionToken());	

        		List<String> marketIds = new ArrayList<String>();
        		
        		for ( MarketCatalogue marketCatalogue : marketCatalogues ) {
        			
            		if ( DateUtils.addMinutes(marketCatalogue.getDescription().getMarketTime(), -1).compareTo(now) < 0 ) {
            			marketIds.add(marketCatalogue.getMarketId());
            		}
        		}
        		
        		PriceProjection priceProjection = new PriceProjection();
        		List<MarketBook> marketBooks = rescriptOperations
        				.listMarketBook(marketIds, priceProjection, null,
        						null, null, applicationKey, session.getSessionToken());
        		Set<PriceData> priceDatas = new HashSet<PriceData>();
        		priceDatas.add(PriceData.EX_BEST_OFFERS);
        		priceProjection.setPriceData(priceDatas);

        		
        		for ( MarketBook marketBook : marketBooks ) {
        			
	                if ( marketBook.getTotalMatched() > betfairConfig.getSizeOfMarket() ) {

		                List<PlaceInstruction> instructions = new ArrayList<PlaceInstruction>();
		                List<Runner> runners = marketBook.getRunners();            	
	        			Runner favourite = runners.get(0);
	            		Runner secondFavourite = runners.get(1);
		                int sizeOfField = runners.size();
		                double localFanciedPriceThreshhold = betfairConfig.getFanciedLargeFieldPriceThreshhold();
		                if ( sizeOfField < betfairConfig.getMediumFieldThrehhold()) {
		                	localFanciedPriceThreshhold = betfairConfig.getFanciedSmallFieldPriceThreshhold();
		                } else {
		                	if ( sizeOfField < betfairConfig.getLargeFieldThrehhold()) {
		                		localFanciedPriceThreshhold = betfairConfig.getFanciedMediumFieldPriceThreshhold();
		                	}
		                }
		                
		                final double fanciedPriceThreshhold = localFanciedPriceThreshhold; 
		                
		                runners.forEach((runner) -> {
		            	//for ( Runner runner : runners ) {            		
		            		if ( runner.getLastPriceTraded() != null &&  runner.getLastPriceTraded() < fanciedPriceThreshhold ) {

		            			LimitOrder limitOrder = new LimitOrder();
		    	                limitOrder.setSize(betfairConfig.getLaySize());
		    	                
//		    	                if ( runner == favourite && favourite.getLastPriceTraded() < secondFavourite.getLastPriceTraded()-betfairConfig.getFavouriteThreshhold() ) {
//			    	                limitOrder.setPrice(getReducedPrice(runner,betfairConfig.getHotFavouriteReductionFactor()));
//		    	                } else {
//			    	                if ( runner == favourite ) {
//				    	                limitOrder.setPrice(getReducedPrice(runner,betfairConfig.getFavouriteReductionFactor()));
//			    	                } else {
//			    	                	if ( runner.getLastPriceTraded() < betfairConfig.getFanciedSmallFieldPriceThreshhold() ) {
//					    	                limitOrder.setPrice(getReducedPrice(runner,betfairConfig.getShortPricedFanciedReductionFactor()));
//			    	                	} else {
//				    	                	if ( runner.getLastPriceTraded() < betfairConfig.getFanciedMediumFieldPriceThreshhold() ) {
//						    	                limitOrder.setPrice(getReducedPrice(runner,betfairConfig.getMediumPricedFanciedReductionFactor()));
//				    	                	} else {
//				    	                		limitOrder.setPrice(getReducedPrice(runner,betfairConfig.getLargePricedFanciedReductionFactor()));
//				    	                	}
//			    	                	}
//			    	                }
//		    	                }

		    	                if ( runner == favourite && favourite.getLastPriceTraded() < secondFavourite.getLastPriceTraded()-betfairConfig.getFavouriteThreshhold() ) {
			    	                limitOrder.setPrice(getReducedPrice(runner,betfairConfig.getHotFavouriteReductionFactor()));
		    	                } else {
			    	                if ( runner == favourite ) {
				    	                limitOrder.setPrice(getReducedPrice(runner,betfairConfig.getFavouriteReductionFactor()));
			    	                } else {
			    	                	if ( runner == secondFavourite && secondFavourite.getLastPriceTraded() < favourite.getLastPriceTraded()+betfairConfig.getFavouriteThreshhold() ) {
					    	                limitOrder.setPrice(getReducedPrice(runner,betfairConfig.getFavouriteReductionFactor()));
			    	                	} 
			    	                }
		    	                }		    	                
		    	                
		    	                if ( limitOrder.getPrice() > 0 ) {
			    	                PlaceInstruction instruction = new PlaceInstruction();
			    	                instruction.setHandicap(0);
			    	                instruction.setSide(Side.LAY);
			    	                instruction.setOrderType(OrderType.LIMIT);		    	                
			    	                limitOrder.setPersistenceType(PersistenceType.PERSIST);
			    	                instruction.setLimitOrder(limitOrder);
			    	                instruction.setSelectionId(runner.getSelectionId());
			    	                instructions.add(instruction);
		    	                }

		    	                System.out.println(runner);
		            		}
		                });			            	
    	
		                PlaceExecutionReport placeBetResult = rescriptOperations.placeOrders(marketBook.getMarketId(), instructions, "3", applicationKey, session.getSessionToken());
		                for ( PlaceInstructionReport placeInstructionReport :  placeBetResult.getInstructionReports() ) {
			                Lay lay = new Lay();
			                lay.setBetId(placeInstructionReport.getBetId());
			                lay.setTimestamp(placeInstructionReport.getPlacedDate());
			                //lay.setSelectionId(placeInstructionReport.getInstructionl().getSelectionId());
			                //lay.setPrice(placeInstructionReport.getInstructionl().getLimitOrder().getPrice());
			                //lay.setMatched(placeInstructionReport.getSizeMatched()>0);
			                lay.setStatus(placeInstructionReport.getStatus());
			                lay.setErrorCode(placeInstructionReport.getErrorCode());
			                lay.setLayType(LayType.FAVOURITES);
			                layRepository.save(lay);
		                }
		                
            		}
        		}
            		
//            		clearFavouriteRepository.saveAll(candidateBets);

        } catch (Throwable anyException) {
        	try { 
        		System.out.println(anyException.toString());
        		gMailer.sendMessage("ERROR!!", anyException + " : " + ExceptionUtils.getStackTrace(anyException) );
        	} catch (MessagingException e ) {
        		System.out.println(e);
        	} catch (IOException e ) { 
        		System.out.println(e);
        	}
        }
    }

	//@Scheduled(fixedDelay = 60000)
    public void layBets2() {
		
        String applicationKey = betfairConfig.getApplicationKey();

        try {
        	
       		session = authenticator.login();

            MarketFilter marketFilter;
            marketFilter = new MarketFilter();
            Set<String> eventTypeIds = new HashSet<String>();
            eventTypeIds.add("7");

            Date now = new Date();
            System.out.println("Time: " + now);
            
            marketFilter = new MarketFilter();
            marketFilter.setEventTypeIds(eventTypeIds);

            TimeRange time = new TimeRange();
            Date endOfDay = DateUtils.setHours(now, 23);
            time.setFrom( now );
            time.setTo(endOfDay);

            marketFilter.setMarketStartTime( time );
            Set<String> countries = new HashSet<String>();
            countries.add("GB");
            Set<String> typesCode = new HashSet<String>();
            typesCode.add("WIN");
            marketFilter.setMarketCountries(countries);
            marketFilter.setMarketTypeCodes(typesCode);

            Set<MarketProjection> marketProjection = new HashSet<MarketProjection>();
            marketProjection.add(MarketProjection.MARKET_DESCRIPTION);

            List<MarketCatalogue> marketCatalogues = null;
            
           	marketCatalogues = rescriptOperations.listMarketCatalogue(marketFilter, marketProjection, MarketSort.FIRST_TO_START, 
           			applicationKey, session.getSessionToken());	

    		List<String> marketIds = new ArrayList<String>();
    		
    		for ( MarketCatalogue marketCatalogue : marketCatalogues ) {
    			
        		if ( DateUtils.addMinutes(marketCatalogue.getDescription().getMarketTime(), -1).compareTo(now) < 0 ) {
        			marketIds.add(marketCatalogue.getMarketId());
        		}
    		}
    		
    		PriceProjection priceProjection = new PriceProjection();
    		List<MarketBook> marketBooks = rescriptOperations
    				.listMarketBook(marketIds, priceProjection, null,
    						null, null, applicationKey, session.getSessionToken());
    		Set<PriceData> priceDatas = new HashSet<PriceData>();
    		priceDatas.add(PriceData.EX_BEST_OFFERS);
    		priceProjection.setPriceData(priceDatas);

		
    		for ( MarketBook marketBook : marketBooks ) {
    			
                if ( marketBook.getTotalMatched() > betfairConfig.getSizeOfMarket() ) {

	                List<PlaceInstruction> instructions = new ArrayList<PlaceInstruction>();
	                List<Runner> runners = marketBook.getRunners();            	
	                int sizeOfField = runners.size();
	                double localFanciedPriceThreshhold = betfairConfig.getFanciedLargeFieldPriceThreshhold();
	                if ( sizeOfField < betfairConfig.getMediumFieldThrehhold()) {
	                	localFanciedPriceThreshhold = betfairConfig.getFanciedSmallFieldPriceThreshhold();
	                } else {
	                	if ( sizeOfField < betfairConfig.getLargeFieldThrehhold()) {
	                		localFanciedPriceThreshhold = betfairConfig.getFanciedMediumFieldPriceThreshhold();
	                	}
	                }
	                
	                final double fanciedPriceThreshhold = localFanciedPriceThreshhold; 
	                
	                runners.forEach((runner) -> {
	            		if ( runner.getLastPriceTraded() != null &&  runner.getLastPriceTraded() < fanciedPriceThreshhold ) {

	            			for ( double oddsOnTier : betfairConfig.getOddsOnTiers()  ) {
		            			LimitOrder limitOrder = new LimitOrder();
		    	                limitOrder.setSize(betfairConfig.getOddsOnLaySize());
		    	                
		    	                limitOrder.setPrice(oddsOnTier);
		    	                PlaceInstruction instruction = new PlaceInstruction();
		    	                instruction.setHandicap(0);
		    	                instruction.setSide(Side.LAY);
		    	                instruction.setOrderType(OrderType.LIMIT);		    	                
		    	                limitOrder.setPersistenceType(PersistenceType.PERSIST);
		    	                instruction.setLimitOrder(limitOrder);
		    	                instruction.setSelectionId(runner.getSelectionId());
		    	                instructions.add(instruction);

		    	                System.out.println(runner);
	            			}
	            		}
	                });			            	
	
	                PlaceExecutionReport placeBetResult = rescriptOperations.placeOrders(marketBook.getMarketId(), instructions, "3", applicationKey, session.getSessionToken());
	                for ( PlaceInstructionReport placeInstructionReport :  placeBetResult.getInstructionReports() ) {
		                Lay lay = new Lay();
		                lay.setBetId(placeInstructionReport.getBetId());
		                lay.setTimestamp(placeInstructionReport.getPlacedDate());
		                //lay.setSelectionId(placeInstructionReport.getInstructionl().getSelectionId());
		                //lay.setPrice(placeInstructionReport.getInstructionl().getLimitOrder().getPrice());
		                //lay.setMatched(placeInstructionReport.getSizeMatched()>0);
		                lay.setStatus(placeInstructionReport.getStatus());
		                lay.setErrorCode(placeInstructionReport.getErrorCode());
		                lay.setLayType(LayType.ODDS_ON);
		                layRepository.save(lay);
	                }
	                
        		}
    		}
        		
//            		clearFavouriteRepository.saveAll(candidateBets);

        } catch (Throwable anyException) {
        	try { 
        		System.out.println(anyException.toString());
        		gMailer.sendMessage("ERROR!!", anyException + " : " + ExceptionUtils.getStackTrace(anyException) );
        	} catch (MessagingException e ) {
        		System.out.println(e);
        	} catch (IOException e ) { 
        		System.out.println(e);
        	}
        }
    }

    private double getReducedPrice( Runner runner, double reductionFactor ) {
    	double reducedPrice = ( runner.getLastPriceTraded() - 1 ) * reductionFactor + 1;
    	BigDecimal price = new BigDecimal(reducedPrice);
    	price = price.setScale(1, RoundingMode.HALF_DOWN);
    	return price.doubleValue();
    }
	
	//@Scheduled(fixedDelay = 60000*30)
    public void updateResults() {
		
        String applicationKey = betfairConfig.getApplicationKey();

        try {
        	
    		Session session = authenticator.login();
    		
    		//rescriptOperations.listMarketBook(marketIds, betIds, priceProjection, orderProjection, matchProjection, currencyCode, matchedSince, appKey, ssoId);

    		layRepository.findAll();
    		
    		
            Date now = new Date();
            System.out.println("Time: " + now);

            MarketFilter marketFilter;
            marketFilter = new MarketFilter();
            Set<String> eventTypeIds = new HashSet<String>();
            eventTypeIds.add("7");

            Iterable<Lay> lays = layRepository.findAll();
            
            for ( Lay lay : lays ) {
            	
            }
            
            System.out.println("\n");
            

        } catch (Throwable anyException) {
        	try { 
        		System.out.println(anyException.toString());
        		gMailer.sendMessage("ERROR!!", anyException + " : " + ExceptionUtils.getStackTrace(anyException) );
        	} catch (MessagingException e ) {
        		System.out.println(e);
        	} catch (IOException e ) { 
        		System.out.println(e);
        	}
        }
    }
}
