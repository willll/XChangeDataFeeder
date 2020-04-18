package exchanges.factories;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import org.knowm.xchange.currency.CurrencyPair;
import org.zeromq.ZContext;

import exchanges.factories.EntryPoint.Exchanges;
import utils.Constants;

public class PoloniexFactory extends GenericStreamingFactory {

	/**
	 *
	 */
	PoloniexFactory() {
		exchange = Exchanges.POLONIEX;
		ticker_pub = "POLONIEX_TICKER_PUB";
		orderbook_pub = "POLONIEX_ORDERBOOK_PUB";
	}

	/**
	 * @param _listCmd
	 * @param _ep
	 * @param _cp
	 * @param _thds
	 * @param _ctx
	 * @throws IOException
	 */
	public static void poloniex(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		GenericStreamingFactory gf_ = ExchangesFactory.getPoloniexFactory();
		GenericStreamingFactory.start(_listCmd,  _ep, _cp, _thds, _ctx, gf_);
	}

	/**
	 *
	 */
	@Override
	public String getEnabled() {
		return Constants.poloniex_enabled;
	}

	/**
	 *
	 */
	@Override
	public String getCurrencyPairs() {
		return Constants.poloniex_currency_pairs;
	}

	/**
	 *
	 */
	@Override
	public String getTickerEnabled() {
		return Constants.poloniex_ticker_enabled;
	}

	/**
	 *
	 */
	@Override
	public String getOrderbookEnabled() {
		return Constants.poloniex_orderbook_enabled;
	}
}