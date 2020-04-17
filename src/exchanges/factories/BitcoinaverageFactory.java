package exchanges.factories;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.knowm.xchange.currency.CurrencyPair;
import org.zeromq.ZContext;

import exchanges.factories.EntryPoint.Exchanges;
import utils.Config;
import utils.Constants;
import utils.CurrencyPairs;

public class BitcoinaverageFactory extends GenericFactory {

	BitcoinaverageFactory() {
		exchange = Exchanges.BITCOINAVERAGE;
		ticker_pub = "BITCOINAVERAGE_TICKER_PUB";
		orderbook_pub = "BITCOINAVERAGE_ORDERBOOK_PUB";
	}

	public static void bitcoinaverage(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitcoinaverage_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.BITCOINAVERAGE, _ep.getExchange(Exchanges.BITCOINAVERAGE).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Bitcoinaverage_cp = _cp;
				String bscp = Config.getInstance().get(Constants.bitcoinaverage_currency_pairs);
				if (bscp != null) {
					Bitcoinaverage_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Bitcoinaverage_cp.add(new CurrencyPair(pair));
					}
				} else {
					Bitcoinaverage_cp = _ep.getExchange(Exchanges.BITCOINAVERAGE).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Bitcoinaverage_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.bitcoinaverage_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getBitcoinaverageFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Bitcoinaverage
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitcoinaverage_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getBitcoinaverageFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Bitcoinaverage
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitcoinaverage_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getBitcoinaverageFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}