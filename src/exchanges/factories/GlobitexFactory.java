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

public class GlobitexFactory extends GenericFactory {

	GlobitexFactory() {
		exchange = Exchanges.GLOBITEX;
		ticker_pub = "GLOBITEX_TICKER_PUB";
		orderbook_pub = "GLOBITEX_ORDERBOOK_PUB";
	}

	public static void globitex(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.globitex_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.GLOBITEX, _ep.getExchange(Exchanges.GLOBITEX).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Globitex_cp = _cp;
				String bscp = Config.getInstance().get(Constants.globitex_currency_pairs);
				if (bscp != null) {
					Globitex_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Globitex_cp.add(new CurrencyPair(pair));
					}
				} else {
					Globitex_cp = _ep.getExchange(Exchanges.GLOBITEX).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Globitex_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.globitex_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getGlobitexFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Globitex
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.globitex_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getGlobitexFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Globitex
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.globitex_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getGlobitexFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}