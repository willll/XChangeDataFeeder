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

public class PoloniexFactory extends GenericStreamingFactory {

	PoloniexFactory() {
		exchange = Exchanges.POLONIEX;
		ticker_pub = "POLONIEX_TICKER_PUB";
		orderbook_pub = "POLONIEX_ORDERBOOK_PUB";
	}

	public static void poloniex(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.poloniex_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.POLONIEX, _ep.getExchange(Exchanges.POLONIEX).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Poloniex_cp = _cp;
				String bscp = Config.getInstance().get(Constants.poloniex_currency_pairs);
				if (bscp != null) {
					Poloniex_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Poloniex_cp.add(new CurrencyPair(pair));
					}
				} else {
					Poloniex_cp = _ep.getExchange(Exchanges.POLONIEX).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Poloniex_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Create a ticker from Poloniex
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.poloniex_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getPoloniexFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Poloniex
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.poloniex_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getPoloniexFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}