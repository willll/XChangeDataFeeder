package exchanges.factories;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import org.knowm.xchange.currency.CurrencyPair;
import org.zeromq.ZContext;

import exchanges.factories.EntryPoint.Exchanges;
import utils.Constants;

public class RippleFactory extends GenericFactory {

	RippleFactory() {
		exchange = Exchanges.RIPPLE;
		ticker_pub = "RIPPLE_TICKER_PUB";
		orderbook_pub = "RIPPLE_ORDERBOOK_PUB";
	}

	/**
	 * @param _listCmd
	 * @param _ep
	 * @param _cp
	 * @param _thds
	 * @param _ctx
	 * @throws IOException
	 */
	public static void ripple(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		GenericFactory gf_ = ExchangesFactory.getRippleFactory();
		GenericFactory.start(_listCmd,  _ep, _cp, _thds, _ctx, gf_);
	}

	/**
	 *
	 */
	@Override
	public String getEnabled() {
		return Constants.ripple_enabled;
	}

	/**
	 *
	 */
	@Override
	public String getCurrencyPairs() {
		return Constants.ripple_currency_pairs;
	}

	/**
	 *
	 */
	@Override
	public String getTickerEnabled() {
		return Constants.ripple_ticker_enabled;
	}

	/**
	 *
	 */
	@Override
	public String getOrderbookEnabled() {
		return Constants.ripple_orderbook_enabled;
	}

	/**
	 *
	 */
	@Override
	public String getRefreshRate() {
		return Constants.ripple_refresh_rate;
	}
}