package com.porkerspicks.horses;

import com.betfair.aping.api.ApiNgRescriptOperations;
import com.betfair.aping.api.BetfairConfig;
import com.betfair.aping.entities.*;
import com.betfair.aping.enums.*;
import com.betfair.aping.exceptions.APINGException;
import com.porkerspicks.horses.domain.Bet;
import com.porkerspicks.horses.domain.BetfairIdentifier;
import com.porkerspicks.horses.domain.HorseSelection;
import com.porkerspicks.horses.domain.Session;
import com.porkerspicks.horses.exceptions.HorseSelectionException;
import com.porkerspicks.horses.repository.BetRepository;

import java.io.IOException;
import java.text.DateFormat;
import java.util.*;

import javax.mail.MessagingException;

import com.porkerspicks.horses.service.BetServiceImpl;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * This is a demonstration class to show a quick demo of the new Betfair API-NG.
 * When you execute the class will: <li>find a market (next horse race in the
 * UK)</li> <li>get prices and runners on this market</li> <li>place a bet on 1
 * runner</li> <li>handle the error</li>
 * 
 */
@Component
public class BetPlacingEngine {

	Logger logger = LoggerFactory.getLogger(BetServiceImpl.class);

	private static String CUSTOMER_REF = "3";
	private static String EVENT_TYPE_HORSE_RACING = "7";

	@Autowired
	private ApiNgRescriptOperations rescriptOperations;

	@Autowired
	private BetfairConfig betfairConfig;

	@Autowired
	private GMailer gMailer;

	@Autowired
	private Authenticator authenticator;

	@Autowired
	private BetRepository betRepository;

	private Session session;


	static List<HorseSelection> horseSelections = new ArrayList<>();

	{
		HorseSelection horseSelection = new HorseSelection();
		horseSelection.setSelection("Imperium");
		horseSelection.setUol(2.72);
		horseSelection.setStartTime(19,15);
		horseSelections.add( horseSelection );
//		horseSelection = new HorseSelection();
//		horseSelection.setSelection("Cafe Sydney");
//		horseSelection.setUol(6.6);
//		horseSelection.setStartTime(15,20);
//		horseSelections.add( horseSelection );
//		horseSelection = new HorseSelection();
//		horseSelection.setSelection("Notoriously Risky");
//		horseSelection.setUol(5.5);
//		horseSelection.setStartTime(16,00);
//		horseSelections.add( horseSelection );
//		horseSelection = new HorseSelection();
//		horseSelection.setSelection("Three Platoon");
//		horseSelection.setUol(3.4);
//		horseSelection.setStartTime(17,0);
//		horseSelections.add( horseSelection );
//		horseSelection = new HorseSelection();
//		horseSelection.setSelection("Daheer");
//		horseSelection.setUol(4.7);
//		horseSelection.setStartTime(18,0);
//		horseSelections.add( horseSelection );
//		horseSelection = new HorseSelection();
//		horseSelection.setSelection("Barden Bella");
//		horseSelection.setUol(3.45);
//		horseSelection.setStartTime(15,05);
//		horseSelections.add( horseSelection );
//		horseSelection = new HorseSelection();
//		horseSelection.setSelection("Onesmoothoperator");
//		horseSelection.setUol(5.1);
//		horseSelection.setStartTime(16,00);
//		horseSelections.add( horseSelection );
	}

	@Scheduled(fixedDelay = 60000)
    public void placeBets() {

        String applicationKey = betfairConfig.getApplicationKey();

        try {

       		session = authenticator.login();

            MarketFilter marketFilter;
            marketFilter = new MarketFilter();
            Set<String> eventTypeIds = new HashSet<String>();
            eventTypeIds.add(EVENT_TYPE_HORSE_RACING);

            List<BetfairHorseSelection> betfairHorseSelections = new ArrayList<BetfairHorseSelection>();

            Date now = new Date();
			logger.debug("Time: " + now);

            List<HorseSelection> unfoundSelections = new ArrayList<HorseSelection>();

            for ( HorseSelection horseSelection : horseSelections ) {

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
                marketProjection.add(MarketProjection.RUNNER_DESCRIPTION);

                List<MarketCatalogue> marketCatalogues = null;

               	marketCatalogues = rescriptOperations.listMarketCatalogue(marketFilter, marketProjection, MarketSort.FIRST_TO_START,
               			applicationKey, session.getSessionToken());

            	BetfairIdentifier betFairIdentifier = findHorseSelection( marketCatalogues, horseSelection.getSelection() );

            	if ( betFairIdentifier != null ) {
            		betfairHorseSelections.add( new BetfairHorseSelection(horseSelection,betFairIdentifier) );
            	} else {
            		unfoundSelections.add(horseSelection);
            	}
            }

            for ( HorseSelection unfoundSelection : unfoundSelections ) {
            	horseSelections.remove(unfoundSelection);
            }


            logger.info("\n");

            if ( betfairHorseSelections.size() == 0 ) {
            	logger.info("No selections found today!\n\n");
            }

			supplementLatestPrices(betfairHorseSelections);

        	List<HorseSelection> betsPlaced = new ArrayList<HorseSelection>();

            for ( BetfairHorseSelection betFairHorseSelection : betfairHorseSelections ) {

            	HorseSelection horseSelection = betFairHorseSelection.getHorseSelection();
            	BetfairIdentifier betfairIdentifier = betFairHorseSelection.getBetfairIdentifier();

	            if ( DateUtils.addSeconds(horseSelection.getStartTime(), -35).compareTo(now) < 0 ) {

					if (betFairHorseSelection.getLastPriceTraded() < horseSelection.getUol() ) {

						logger.info("\nTime to trigger bet for " + horseSelection.getSelection() + "\n\n");

						String marketIdChosen = betfairIdentifier.getMarketId();
						long selectionId = betfairIdentifier.getSelectionId();
						logger.info("Placing bet on marketId: "+marketIdChosen+" with selectionId: "+selectionId+"...\n\n" + " and UOL of " + Double.toString(horseSelection.getUol()) + ". Quoted price: " + Double.toString(betFairHorseSelection.getLastPriceTraded()) );
						List<PlaceInstruction> instructions = new ArrayList<PlaceInstruction>();
						PlaceInstruction instruction = new PlaceInstruction();
						instruction.setHandicap(0);
						instruction.setSide(Side.BACK);
						instruction.setOrderType(OrderType.LIMIT);

						LimitOrder limitOrder = new LimitOrder();
						limitOrder.setSize(betfairConfig.getBetSize());
						limitOrder.setPrice(betFairHorseSelection.getLastPriceTraded()-1);
						limitOrder.setPersistenceType(PersistenceType.LAPSE);
						instruction.setLimitOrder(limitOrder);
						instruction.setSelectionId(selectionId);
						instructions.add(instruction);

						String customerRef = "3";

						PlaceExecutionReport placeBetResult = rescriptOperations.placeOrders(marketIdChosen, instructions, customerRef, applicationKey, session.getSessionToken());

						PlaceInstructionReport placeInstructionReport = placeBetResult.getInstructionReports().get(0);

						Bet bet = new Bet();
						bet.setBetId(placeInstructionReport.getBetId());
						bet.setTimestamp(placeInstructionReport.getPlacedDate());
						bet.setSelection(horseSelection.getSelection());
						bet.setMbo(horseSelection.getMbo());
						bet.setUol(horseSelection.getUol());
						bet.setAveragePriceMatched(placeInstructionReport.getAveragePriceMatched());
						bet.setSize(limitOrder.getSize());
						//bet.setSize(limitOrder.getSize());
						bet.setSizeMatched(placeInstructionReport.getSizeMatched());
						bet.setStatus(placeInstructionReport.getStatus());
						bet.setErrorCode(placeInstructionReport.getErrorCode());
						horseSelection.setPlacedBet(bet);

						// Handling the operation result
						if (placeBetResult.getStatus() == ExecutionReportStatus.SUCCESS) {
							logger.info("**************************");
							logger.info("Your bet has been placed!!");
							logger.info("**************************");
							logger.info(placeBetResult.getInstructionReports().get(0).toString());
							betsPlaced.add(horseSelection);
						} else if (placeBetResult.getStatus() == ExecutionReportStatus.FAILURE) {
							logger.info("Your bet has NOT been placed :*( ");
							String errormessage = "The error is: " + placeBetResult.getErrorCode() + ": " + placeBetResult.getErrorCode().getMessage();
							logger.info(errormessage);
							gMailer.sendMessage("ERROR!!", errormessage);
						}
					}

	            } else {
	                logger.info("No trigger for " + horseSelection.getSelection());
	            }
            }

            for ( HorseSelection betPlaced : betsPlaced ) {
            	horseSelections.remove(betPlaced);
            	betRepository.save(betPlaced.getPlacedBet());
            }

        } catch (Throwable anyException) {
        	try {
        		logger.info(anyException.toString());
        		gMailer.sendMessage("ERROR!!", anyException + " : " + anyException );
        		anyException.printStackTrace(System.out);
        	} catch (MessagingException e ) {
        		logger.info(e.toString());
        	} catch (IOException e ) {
        		logger.info(e.toString());
        	}
        }
    }

	private void supplementLatestPrices( List<BetfairHorseSelection> betfairHorseSelections ) throws APINGException {

		List<String> marketIds = new ArrayList<String>();

		for (BetfairHorseSelection betfairHorseSelection : betfairHorseSelections) {
			marketIds.add(betfairHorseSelection.getBetfairIdentifier().getMarketId());
		}

		PriceProjection priceProjection = new PriceProjection();
		Set<PriceData> priceDatas = new HashSet<PriceData>();
		priceDatas.add(PriceData.EX_BEST_OFFERS);
		priceProjection.setPriceData(priceDatas);
		List<MarketBook> marketBooks = rescriptOperations
				.listMarketBook(marketIds, priceProjection, null,
						null, null, betfairConfig.getApplicationKey(), session.getSessionToken());

		for ( BetfairHorseSelection betfairHorseSelection : betfairHorseSelections ) {
			for ( MarketBook marketBook : marketBooks ) {
				marketBook.getRunners().forEach((runner) -> {
					if (runner.getSelectionId().equals(betfairHorseSelection.getBetfairIdentifier().getSelectionId())) {
						betfairHorseSelection.setLastPriceTraded(runner.getLastPriceTraded());
					}
				});
			}
		}
	}

	//@Scheduled(fixedDelay = 60000)
    public void updateResults() {
		
        String applicationKey = betfairConfig.getApplicationKey();

        try {
        	
    		Session session = authenticator.login();
    		
            Set<String> eventTypeIds = new HashSet<String>();
            eventTypeIds.add(EVENT_TYPE_HORSE_RACING);

            TimeRange settledDateRange = new TimeRange();
            Date startofDay= new Date();
            DateUtils.setHours(startofDay, 1);
            settledDateRange.setFrom( startofDay );
    		
    		ClearedOrderSummaryReport clearedOrderSummaryReport = rescriptOperations.listClearedOrders( BetStatus.SETTLED, eventTypeIds, null, CUSTOMER_REF, applicationKey, session.getSessionToken());

                  
            for ( ClearedOrderSummary clearedOrderSummary : clearedOrderSummaryReport.getClearedOrders() ) {
                logger.info(clearedOrderSummary.getPriceRequested() + "\n");                
            }
            

        } catch (Throwable anyException) {
        	try { 
        		logger.info(anyException.toString());
        		gMailer.sendMessage("ERROR!!", anyException.toString());
        	} catch (MessagingException e ) {
        		logger.info(e.toString());
        	} catch (IOException e ) { 
        		logger.info(e.toString());
        	}
        }
    }
	
	private BetfairIdentifier findHorseSelection( List<MarketCatalogue> marketCatalogues, String horseName ) {
		
		BetfairIdentifier horseSelection = null;
		
		for ( MarketCatalogue marketCatalogue : marketCatalogues ) {
			for ( RunnerCatalog runner : marketCatalogue.getRunners() ) {
				if ( horseName.equals( runner.getRunnerName() ) ) {
					if ( horseSelection != null ) {
						throw new HorseSelectionException("Duplicate found for selection " + horseName + " in race " + marketCatalogue.getMarketName() + ", " + marketCatalogue.getDescription() );
					}
					horseSelection = new BetfairIdentifier();
					horseSelection.setSelectionId( runner.getSelectionId() );
					horseSelection.setMarketId( marketCatalogue.getMarketId() );
					logger.info("Found Selection: " + horseName );
				}
			}
		}
		
		if ( horseSelection == null ) {
			String message = "No market found for selection " + horseName + ".";
    		logger.info(message);
        	try { 
        		gMailer.sendMessage("ERROR!!", message );
        	} catch (MessagingException e ) {
        		logger.info(e.toString());
        	} catch (IOException e ) { 
        		logger.info(e.toString());
        	}
		}
		
		return horseSelection;
	}

    private class BetfairHorseSelection {
    	
    	private HorseSelection horseSelection;
		private BetfairIdentifier betfairIdentifier;
		private Double lastPriceTraded;
    	
    	public BetfairHorseSelection(HorseSelection horseSelection, BetfairIdentifier betfairIdentifier) {
			super();
			this.horseSelection = horseSelection;
			this.betfairIdentifier = betfairIdentifier;
		}

    	public HorseSelection getHorseSelection() {
			return horseSelection;
		}
		public BetfairIdentifier getBetfairIdentifier() {
			return betfairIdentifier;
		}
		public Double getLastPriceTraded() { return lastPriceTraded; }
		public void setLastPriceTraded(Double lastPriceTraded) { this.lastPriceTraded = lastPriceTraded; }
	}
}