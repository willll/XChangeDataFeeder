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

public class LgoFactory extends GenericStreamingFactory {

	LgoFactory() {
		exchange = Exchanges.LGO;
		ticker_pub = "LGO_TICKER_PUB";
		orderbook_pub = "LGO_ORDERBOOK_PUB";
	}

	public static void lgo(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.lgo_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.LGO, _ep.getExchange(Exchanges.LGO).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Lgo_cp = _cp;
				String bscp = Config.getInstance().get(Constants.lgo_currency_pairs);
				if (bscp != null) {
					Lgo_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Lgo_cp.add(new CurrencyPair(pair));
					}
				} else {
					Lgo_cp = _ep.getExchange(Exchanges.LGO).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Lgo_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Create a ticker from Lgo
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.lgo_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getLgoFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Lgo
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.lgo_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getLgoFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}