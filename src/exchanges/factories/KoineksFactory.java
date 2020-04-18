package exchanges.factories;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import org.knowm.xchange.currency.CurrencyPair;
import org.zeromq.ZContext;

import exchanges.factories.EntryPoint.Exchanges;
import utils.Constants;

public class KoineksFactory extends GenericFactory {

	KoineksFactory() {
		exchange = Exchanges.KOINEKS;
		ticker_pub = "KOINEKS_TICKER_PUB";
		orderbook_pub = "KOINEKS_ORDERBOOK_PUB";
	}

	/**
	 * @param _listCmd
	 * @param _ep
	 * @param _cp
	 * @param _thds
	 * @param _ctx
	 * @throws IOException
	 */
	public static void koineks(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		GenericFactory gf_ = ExchangesFactory.getKoineksFactory();
		GenericFactory.start(_listCmd,  _ep, _cp, _thds, _ctx, gf_);
	}

	/**
	 *
	 */
	@Override
	public String getEnabled() {
		return Constants.koineks_enabled;
	}

	/**
	 *
	 */
	@Override
	public String getCurrencyPairs() {
		return Constants.koineks_currency_pairs;
	}

	/**
	 *
	 */
	@Override
	public String getTickerEnabled() {
		return Constants.koineks_ticker_enabled;
	}

	/**
	 *
	 */
	@Override
	public String getOrderbookEnabled() {
		return Constants.koineks_orderbook_enabled;
	}

	/**
	 *
	 */
	@Override
	public String getRefreshRate() {
		return Constants.koineks_refresh_rate;
	}
}