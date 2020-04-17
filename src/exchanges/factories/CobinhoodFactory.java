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

public class CobinhoodFactory extends GenericFactory {

	CobinhoodFactory() {
		exchange = Exchanges.COBINHOOD;
		ticker_pub = "COBINHOOD_TICKER_PUB";
		orderbook_pub = "COBINHOOD_ORDERBOOK_PUB";
	}

	public static void cobinhood(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.cobinhood_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.COBINHOOD, _ep.getExchange(Exchanges.COBINHOOD).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Cobinhood_cp = _cp;
				String bscp = Config.getInstance().get(Constants.cobinhood_currency_pairs);
				if (bscp != null) {
					Cobinhood_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Cobinhood_cp.add(new CurrencyPair(pair));
					}
				} else {
					Cobinhood_cp = _ep.getExchange(Exchanges.COBINHOOD).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Cobinhood_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.cobinhood_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getCobinhoodFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Cobinhood
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.cobinhood_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getCobinhoodFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Cobinhood
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.cobinhood_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getCobinhoodFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}