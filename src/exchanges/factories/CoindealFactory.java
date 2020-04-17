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

public class CoindealFactory extends GenericFactory {

	CoindealFactory() {
		exchange = Exchanges.COINDEAL;
		ticker_pub = "COINDEAL_TICKER_PUB";
		orderbook_pub = "COINDEAL_ORDERBOOK_PUB";
	}

	public static void coindeal(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.coindeal_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.COINDEAL, _ep.getExchange(Exchanges.COINDEAL).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Coindeal_cp = _cp;
				String bscp = Config.getInstance().get(Constants.coindeal_currency_pairs);
				if (bscp != null) {
					Coindeal_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Coindeal_cp.add(new CurrencyPair(pair));
					}
				} else {
					Coindeal_cp = _ep.getExchange(Exchanges.COINDEAL).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Coindeal_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.coindeal_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getAcxFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Coindeal
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.coindeal_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Coindeal
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.coindeal_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}