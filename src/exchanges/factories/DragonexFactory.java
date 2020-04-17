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

public class DragonexFactory extends GenericFactory {

	DragonexFactory() {
		exchange = Exchanges.DRAGONEX;
		ticker_pub = "DRAGONEX_TICKER_PUB";
		orderbook_pub = "DRAGONEX_ORDERBOOK_PUB";
	}

	public static void dragonex(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.dragonex_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.DRAGONEX, _ep.getExchange(Exchanges.DRAGONEX).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Dragonex_cp = _cp;
				String bscp = Config.getInstance().get(Constants.dragonex_currency_pairs);
				if (bscp != null) {
					Dragonex_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Dragonex_cp.add(new CurrencyPair(pair));
					}
				} else {
					Dragonex_cp = _ep.getExchange(Exchanges.DRAGONEX).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Dragonex_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.dragonex_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getDragonexFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Dragonex
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.dragonex_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getDragonexFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Dragonex
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.dragonex_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getDragonexFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}