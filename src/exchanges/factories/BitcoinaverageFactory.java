package exchanges.factories;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import org.knowm.xchange.currency.CurrencyPair;
import org.zeromq.ZContext;

import exchanges.factories.EntryPoint.Exchanges;
import utils.Constants;

public class BitcoinaverageFactory extends GenericFactory {

	BitcoinaverageFactory() {
		exchange = Exchanges.BITCOINAVERAGE;
		ticker_pub = "BITCOINAVERAGE_TICKER_PUB";
		orderbook_pub = "BITCOINAVERAGE_ORDERBOOK_PUB";
	}

	/**
	 * @param _listCmd
	 * @param _ep
	 * @param _cp
	 * @param _thds
	 * @param _ctx
	 * @throws IOException
	 */
	public static void bitcoinaverage(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		GenericFactory gf_ = ExchangesFactory.getBitcoinaverageFactory();
		GenericFactory.start(_listCmd,  _ep, _cp, _thds, _ctx, gf_);
	}

	/**
	 *
	 */
	@Override
	public String getEnabled() {
		return Constants.bitcoinaverage_enabled;
	}

	/**
	 *
	 */
	@Override
	public String getCurrencyPairs() {
		return Constants.bitcoinaverage_currency_pairs;
	}

	/**
	 *
	 */
	@Override
	public String getTickerEnabled() {
		return Constants.bitcoinaverage_ticker_enabled;
	}

	/**
	 *
	 */
	@Override
	public String getOrderbookEnabled() {
		return Constants.bitcoinaverage_orderbook_enabled;
	}

	/**
	 *
	 */
	@Override
	public String getRefreshRate() {
		return Constants.bitcoinaverage_refresh_rate;
	}
}