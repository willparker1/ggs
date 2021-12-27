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

import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.xssf.usermodel.XSSFAutoFilter;
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
public class BetsReader {
	
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
		horseSelection.setSelection("Flamboyant Joyaux");
		horseSelection.setMbo(3.5);
		horseSelection.setStartTime(15,55);
		horseSelections.add( horseSelection );
		horseSelection = new HorseSelection();
		horseSelection.setSelection("Regional");
		horseSelection.setMbo(3.0);
		horseSelection.setStartTime(16,05);
		horseSelections.add( horseSelection );
//		horseSelection = new HorseSelection();
//		horseSelection.setSelection("Near Kettering");
//		horseSelection.setMbo(3.5);
//		horseSelection.setStartTime(17,00);
//		horseSelections.add( horseSelection );
//		horseSelection = new HorseSelection();
//		horseSelection.setSelection("Glamorous Force");
//		horseSelection.setMbo(3.5);
//		horseSelection.setStartTime(16,35);
//		horseSelections.add( horseSelection );
//		horseSelection = new HorseSelection();
//		horseSelection.setSelection("Sky Blue Thinking");
//		horseSelection.setMbo(3.5);
//		horseSelection.setStartTime(18,00);
//		horseSelections.add( horseSelection );
//		horseSelection = new HorseSelection();
//		horseSelection.setSelection("Miami Joy");
//		horseSelection.setMbo(3.5);
//		horseSelection.setStartTime(19,00);
//		horseSelections.add( horseSelection );
//		horseSelection = new HorseSelection();
//		horseSelection.setSelection("Lankaran");
//		horseSelection.setMbo(2.5);
//		horseSelection.setStartTime(19,8);
//		horseSelections.add( horseSelection );
//		horseSelection = new HorseSelection();
//		horseSelection.setSelection("Un Prophete");
//		horseSelection.setMbo(2.0);
//		horseSelection.setStartTime(16,28);
//		horseSelections.add( horseSelection );
//		horseSelection = new HorseSelection();
//		horseSelection.setSelection("Dusty Damsel");
//		horseSelection.setMbo(2.5);
//		horseSelection.setStartTime(20,00);
//		horseSelections.add( horseSelection );
	}
	
	@Scheduled(fixedDelay = 60000)
    public void placeBets() {
		
        String applicationKey = betfairConfig.getApplicationKey();

		XSSFAutoFilter f;

        try {
        	
       		session = authenticator.login();

            MarketFilter marketFilter;
            marketFilter = new MarketFilter();
            Set<String> eventTypeIds = new HashSet<String>();
            eventTypeIds.add(EVENT_TYPE_HORSE_RACING);

            List<BetfairHorseSelection> betfairHorseSelections = new ArrayList<BetfairHorseSelection>();

            Date now = new Date();
            System.out.println("Time: " + now);
            
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
            

            System.out.println("\n");
            
            if ( betfairHorseSelections.size() == 0 ) {
            	System.out.println("No selections found today!\n\n");
            } 
            
        	List<HorseSelection> betsPlaced = new ArrayList<HorseSelection>();
            
            for ( BetfairHorseSelection betFairHorseSelection : betfairHorseSelections ) {
            	
            	HorseSelection horseSelection = betFairHorseSelection.getHorseSelection();
            	BetfairIdentifier betfairIdentifier = betFairHorseSelection.getBetfairIdentifier();
            	
	            if ( DateUtils.addMinutes(horseSelection.getStartTime(), -1).compareTo(now) < 0 ) {
	            		            	
	                System.out.println("\nTime to trigger bet for " + horseSelection.getSelection() + "\n\n");
	                
		            String marketIdChosen = betfairIdentifier.getMarketId();
		            long selectionId = betfairIdentifier.getSelectionId();
	                System.out.println("Placing bet on marketId: "+marketIdChosen+" with selectionId: "+selectionId+"...\n\n" + " and MBO of " + Double.toString(horseSelection.getMbo()) );
	                List<PlaceInstruction> instructions = new ArrayList<PlaceInstruction>();
	                PlaceInstruction instruction = new PlaceInstruction();
	                instruction.setHandicap(0);
	                instruction.setSide(Side.BACK);
	                instruction.setOrderType(OrderType.LIMIT_ON_CLOSE);
	                
	                LimitOnCloseOrder limitOnCloseOrder = new LimitOnCloseOrder();
	                limitOnCloseOrder.setLiability(betfairConfig.getBetSize());
	                limitOnCloseOrder.setPrice(horseSelection.getMbo());
	                instruction.setLimitOnCloseOrder(limitOnCloseOrder);
	                instruction.setSelectionId(selectionId);
	                instructions.add(instruction);
//	                PlaceInstruction instruction = new PlaceInstruction();
//	                instruction.setHandicap(0);
//	                instruction.setSide(Side.BACK);
//	                instruction.setOrderType(OrderType.LIMIT);
//	                
//	                LimitOrder limitOrder = new LimitOrder();
//	                limitOrder.setSize(betfairConfig.getBetSize());
//	                limitOrder.setPrice(horseSelection.getMbo());
//	                limitOrder.setPersistenceType(PersistenceType.LAPSE);
//	                instruction.setLimitOrder(limitOrder);
//	                instruction.setSelectionId(selectionId);
//	                instructions.add(instruction);
	
	                String customerRef = "3";
	
	                PlaceExecutionReport placeBetResult = rescriptOperations.placeOrders(marketIdChosen, instructions, customerRef, applicationKey, session.getSessionToken());
	                
	                PlaceInstructionReport placeInstructionReport = placeBetResult.getInstructionReports().get(0);
	                
	                Bet bet = new Bet();
	                bet.setBetId(placeInstructionReport.getBetId());
	                bet.setTimestamp(placeInstructionReport.getPlacedDate());
	                bet.setSelection(horseSelection.getSelection());
	                bet.setMbo(horseSelection.getMbo());
	                bet.setAveragePriceMatched(placeInstructionReport.getAveragePriceMatched());
	                bet.setSize(limitOnCloseOrder.getLiability());
	                //bet.setSize(limitOrder.getSize());
	                bet.setSizeMatched(placeInstructionReport.getSizeMatched());
	                bet.setStatus(placeInstructionReport.getStatus());
	                bet.setErrorCode(placeInstructionReport.getErrorCode());
	                horseSelection.setPlacedBet(bet);
	
	                // Handling the operation result
	                if (placeBetResult.getStatus() == ExecutionReportStatus.SUCCESS) {
	                    System.out.println("**************************");
	                    System.out.println("Your bet has been placed!!");
	                    System.out.println("**************************");
	                    System.out.println(placeBetResult.getInstructionReports());
	                    betsPlaced.add(horseSelection);
	                } else if (placeBetResult.getStatus() == ExecutionReportStatus.FAILURE) {
	                    System.out.println("Your bet has NOT been placed :*( ");
	                    String errormessage = "The error is: " + placeBetResult.getErrorCode() + ": " + placeBetResult.getErrorCode().getMessage();
	                    System.out.println(errormessage);
	                    gMailer.sendMessage("ERROR!!", errormessage);
	                }
	            
	            } else {
	                System.out.println("No trigger for " + horseSelection.getSelection());
	            }
            }

            for ( HorseSelection betPlaced : betsPlaced ) {
            	horseSelections.remove(betPlaced);
            	betRepository.save(betPlaced.getPlacedBet());
            }

        } catch (Throwable anyException) {
        	try { 
        		System.out.println(anyException.toString());
        		gMailer.sendMessage("ERROR!!", anyException + " : " + anyException );
        		anyException.printStackTrace(System.out);
        	} catch (MessagingException e ) {
        		System.out.println(e);
        	} catch (IOException e ) { 
        		System.out.println(e);
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
                System.out.println(clearedOrderSummary.getPriceRequested() + "\n");                
            }
            

        } catch (Throwable anyException) {
        	try { 
        		System.out.println(anyException.toString());
        		gMailer.sendMessage("ERROR!!", anyException.toString());
        	} catch (MessagingException e ) {
        		System.out.println(e);
        	} catch (IOException e ) { 
        		System.out.println(e);
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
					System.out.println("Found Selection: " + horseName );
				}
			}
		}
		
		if ( horseSelection == null ) {
			String message = "No market found for selection " + horseName + ".";
    		System.out.println(message);
        	try { 
        		gMailer.sendMessage("ERROR!!", message );
        	} catch (MessagingException e ) {
        		System.out.println(e);
        	} catch (IOException e ) { 
        		System.out.println(e);
        	}
		}
		
		return horseSelection;
	}

    private class BetfairHorseSelection {
    	
    	private HorseSelection horseSelection;
		private BetfairIdentifier betfairIdentifier;
    	
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
    }
}
