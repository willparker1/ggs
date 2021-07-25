package com.betfair.aping.api;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.hibernate.sql.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.betfair.aping.entities.AccountStatementReport;
import com.betfair.aping.entities.ClearedOrderSummaryReport;
import com.betfair.aping.entities.EventTypeResult;
import com.betfair.aping.entities.MarketBook;
import com.betfair.aping.entities.MarketCatalogue;
import com.betfair.aping.entities.MarketFilter;
import com.betfair.aping.entities.PlaceExecutionReport;
import com.betfair.aping.entities.PlaceInstruction;
import com.betfair.aping.entities.PriceProjection;
import com.betfair.aping.entities.TimeRange;
import com.betfair.aping.enums.ApiNgOperation;
import com.betfair.aping.enums.BetStatus;
import com.betfair.aping.enums.IncludeItem;
import com.betfair.aping.enums.MarketProjection;
import com.betfair.aping.enums.MarketSort;
import com.betfair.aping.enums.MatchProjection;
import com.betfair.aping.enums.OrderProjection;
import com.betfair.aping.enums.Wallet;
import com.betfair.aping.exceptions.APINGException;
import com.betfair.aping.util.JsonConverter;
import com.google.gson.reflect.TypeToken;

@Component
public class ApiNgRescriptOperations {

	protected final String FILTER = "filter";
	protected final String LOCALE = "locale";
	protected final String SORT = "sort";
	protected final String MAX_RESULT = "maxResults";
	protected final String MARKET_IDS = "marketIds";
	protected final String MARKET_ID = "marketId";
	protected final String MATCHED_SINCE = "matchedSince";
	protected final String BET_IDS = "betIds";
	protected final String INSTRUCTIONS = "instructions";
	protected final String CUSTOMER_REF = "customerRef";
	protected final String MARKET_PROJECTION = "marketProjection";
	protected final String PRICE_PROJECTION = "priceProjection";
	protected final String MATCH_PROJECTION = "matchProjection";
	protected final String ORDER_PROJECTION = "orderProjection";
	protected final String BET_STATUS = "betStatus";
	protected final String EVENT_TYPE_IDS = "eventTypeIds";
	protected final String SETTLED_DATE_RANGE = "settledDateRange";
	protected final String CUSTOMER_ORDER_REFS = "customerOrderRefs";
	protected final String FROM_RECORD = "fromRecord";
	protected final String RECORD_COUNT = "recordCount";
	protected final String ITEM_DATA_RANGE = "itemDateRange";
	protected final String INCLUDE_ITEM = "includeItem";
	protected final String WALLET = "wallet";
	

	protected final String locale = Locale.getDefault().toString();

	@Autowired
	private BetfairConfig betfairConfig;

	@Autowired
	private HttpUtil requester;

	public BetfairConfig getBetfairConfig() {
		return betfairConfig;
	}

	public void setBetfairConfig(BetfairConfig betfairConfig) {
		this.betfairConfig = betfairConfig;
	}

	private ApiNgRescriptOperations() {
	}

	public List<EventTypeResult> listEventTypes(MarketFilter filter, String appKey, String ssoId)
			throws APINGException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(FILTER, filter);
		params.put(LOCALE, locale);
		String result = makeRequest(ApiNgOperation.LISTEVENTTYPES.getOperationName(), params, appKey, ssoId);
		if (betfairConfig.isDebug())
			System.out.println("\nResponse: " + result);

		List<EventTypeResult> container = JsonConverter.convertFromJson(result, new TypeToken<List<EventTypeResult>>() {
		}.getType());

		return container;

	}

	public List<MarketBook> listMarketBook(List<String> marketIds, PriceProjection priceProjection,
			OrderProjection orderProjection, MatchProjection matchProjection, String currencyCode, String appKey,
			String ssoId) throws APINGException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(LOCALE, locale);
		params.put(MARKET_IDS, marketIds);
		params.put(PRICE_PROJECTION, priceProjection);
		params.put(ORDER_PROJECTION, orderProjection);
		params.put(MATCH_PROJECTION, matchProjection);
		// params.put(BET_IDS, betIds);
		// params.put(MATCHED_SINCE, matchedSince);
		String result = makeRequest(ApiNgOperation.LISTMARKETBOOK.getOperationName(), params, appKey, ssoId);
		if (betfairConfig.isDebug())
			System.out.println("\nResponse: " + result);

		List<MarketBook> container = JsonConverter.convertFromJson(result, new TypeToken<List<MarketBook>>() {
		}.getType());

		return container;

	}

	public List<MarketCatalogue> listMarketCatalogue(MarketFilter filter, Set<MarketProjection> marketProjection,
			MarketSort sort, String appKey, String ssoId) throws APINGException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(LOCALE, locale);
		params.put(FILTER, filter);
		params.put(SORT, sort);
		params.put(MAX_RESULT, 999);
		params.put(MARKET_PROJECTION, marketProjection);
		String result = makeRequest(ApiNgOperation.LISTMARKETCATALOGUE.getOperationName(), params, appKey, ssoId);
		if (betfairConfig.isDebug())
			System.out.println("\nResponse: " + result);

		List<MarketCatalogue> container = JsonConverter.convertFromJson(result, new TypeToken<List<MarketCatalogue>>() {
		}.getType());

		return container;

	}

	public PlaceExecutionReport placeOrders(String marketId, List<PlaceInstruction> instructions, String customerRef,
			String appKey, String ssoId) throws APINGException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(LOCALE, locale);
		params.put(MARKET_ID, marketId);
		params.put(INSTRUCTIONS, instructions);
		params.put(CUSTOMER_REF, customerRef);
		String result = makeRequest(ApiNgOperation.PLACORDERS.getOperationName(), params, appKey, ssoId);
		if (betfairConfig.isDebug())
			System.out.println("\nResponse: " + result);

		return JsonConverter.convertFromJson(result, PlaceExecutionReport.class);

	}

	public ClearedOrderSummaryReport listClearedOrders(BetStatus betStatus, Set<String> eventTypeIds,
			TimeRange settledDateRange, String customerRef, String appKey, String ssoId) throws APINGException {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(LOCALE, locale);
		params.put(BET_STATUS, betStatus);
		params.put(EVENT_TYPE_IDS, eventTypeIds);
		if (settledDateRange != null) {
			params.put(SETTLED_DATE_RANGE, settledDateRange);
		}
//        Set<String> customerOrderRefs = new HashSet<String>();
//        customerOrderRefs.add(CUSTOMER_REF);
//        params.put(CUSTOMER_ORDER_REFS, customerOrderRefs);
		String result = makeRequest(ApiNgOperation.LISTCLEAREDORDERS.getOperationName(), params, appKey, ssoId);
		if (betfairConfig.isDebug())
			System.out.println("\nResponse: " + result);

		return JsonConverter.convertFromJson(result, ClearedOrderSummaryReport.class);

	}

	public AccountStatementReport getAccountStatement(int fromRecord, int recordCount, TimeRange itemDateRange, IncludeItem includeItem, Wallet wallet, String appKey,
			String ssoId) throws APINGException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(LOCALE, locale);
		params.put(FROM_RECORD, fromRecord);
		params.put(RECORD_COUNT, recordCount);
		params.put(ITEM_DATA_RANGE, itemDateRange);
		params.put(INCLUDE_ITEM, includeItem);
		params.put(WALLET, wallet);
		String result = makeRequest(ApiNgOperation.GETACCOUNTSTATEMENT.getOperationName(), params, appKey, ssoId);
		if (betfairConfig.isDebug())
			System.out.println("\nResponse: " + result);

		AccountStatementReport accountStatementReport = JsonConverter.convertFromJson(result, AccountStatementReport.class );

		return accountStatementReport;

	}

	protected String makeRequest(String operation, Map<String, Object> params, String appKey, String ssoToken)
			throws APINGException {
		String requestString;
		// Handling the Rescript request
		params.put("id", 1);

		requestString = JsonConverter.convertToJson(params);
		if (betfairConfig.isDebug())
			System.out.println("\nRequest: " + requestString);

		// We need to pass the "sendPostRequest" method a string in util format:
		// requestString

		String response = requester.sendPostRequestRescript(requestString, operation, appKey, ssoToken);
		if (response != null)
			return response;
		else
			throw new APINGException();
	}

}
