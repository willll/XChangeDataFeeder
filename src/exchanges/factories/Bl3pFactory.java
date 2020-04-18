package exchanges.factories;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import org.knowm.xchange.currency.CurrencyPair;
import org.zeromq.ZContext;

import exchanges.factories.EntryPoint.Exchanges;
import utils.Constants;

public class Bl3pFactory extends GenericFactory {

	Bl3pFactory() {
		exchange = Exchanges.BL3P;
		ticker_pub = "BL3P_TICKER_PUB";
		orderbook_pub = "BL3P_ORDERBOOK_PUB";
	}

	/**
	 * @param _listCmd
	 * @param _ep
	 * @param _cp
	 * @param _thds
	 * @param _ctx
	 * @throws IOException
	 */
	public static void bl3p(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		GenericFactory gf_ = ExchangesFactory.getBl3pFactory();
		GenericFactory.start(_listCmd,  _ep, _cp, _thds, _ctx, gf_);
	}

	/**
	 *
	 */
	@Override
	public String getEnabled() {
		return Constants.bl3p_enabled;
	}

	/**
	 *
	 */
	@Override
	public String getCurrencyPairs() {
		return Constants.bl3p_currency_pairs;
	}

	/**
	 *
	 */
	@Override
	public String getTickerEnabled() {
		return Constants.bl3p_ticker_enabled;
	}

	/**
	 *
	 */
	@Override
	public String getOrderbookEnabled() {
		return Constants.bl3p_orderbook_enabled;
	}

	/**
	 *
	 */
	@Override
	public String getRefreshRate() {
		return Constants.bl3p_refresh_rate;
	}
}