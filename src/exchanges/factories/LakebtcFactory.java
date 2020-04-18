package exchanges.factories;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import org.knowm.xchange.currency.CurrencyPair;
import org.zeromq.ZContext;

import exchanges.factories.EntryPoint.Exchanges;
import utils.Constants;

public class LakebtcFactory extends GenericFactory {

	LakebtcFactory() {
		exchange = Exchanges.LAKEBTC;
		ticker_pub = "LAKEBTC_TICKER_PUB";
		orderbook_pub = "LAKEBTC_ORDERBOOK_PUB";
	}

	/**
	 * @param _listCmd
	 * @param _ep
	 * @param _cp
	 * @param _thds
	 * @param _ctx
	 * @throws IOException
	 */
	public static void LakeBTC(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		GenericFactory gf_ = ExchangesFactory.getLakebtcFactory();
		GenericFactory.start(_listCmd,  _ep, _cp, _thds, _ctx, gf_);
	}

	/**
	 *
	 */
	@Override
	public String getEnabled() {
		return Constants.LakeBTC_enabled;
	}

	/**
	 *
	 */
	@Override
	public String getCurrencyPairs() {
		return Constants.LakeBTC_currency_pairs;
	}

	/**
	 *
	 */
	@Override
	public String getTickerEnabled() {
		return Constants.LakeBTC_ticker_enabled;
	}

	/**
	 *
	 */
	@Override
	public String getOrderbookEnabled() {
		return Constants.LakeBTC_orderbook_enabled;
	}

	/**
	 *
	 */
	@Override
	public String getRefreshRate() {
		return Constants.LakeBTC_refresh_rate;
	}
}