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

public class HitbtcFactory extends GenericStreamingFactory {

	HitbtcFactory() {
		exchange = Exchanges.HITBTC;
		ticker_pub = "HITBTC_TICKER_PUB";
		orderbook_pub = "HITBTC_ORDERBOOK_PUB";
	}

	public static void hitbtc(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.hitbtc_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.HITBTC, _ep.getExchange(Exchanges.HITBTC).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Hitbtc_cp = _cp;
				String bscp = Config.getInstance().get(Constants.hitbtc_currency_pairs);
				if (bscp != null) {
					Hitbtc_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Hitbtc_cp.add(new CurrencyPair(pair));
					}
				} else {
					Hitbtc_cp = _ep.getExchange(Exchanges.HITBTC).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Hitbtc_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Create a ticker from Hitbtc
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.hitbtc_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getHitbtcFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Hitbtc
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.hitbtc_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getHitbtcFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}