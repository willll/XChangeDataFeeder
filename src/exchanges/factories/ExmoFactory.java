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

public class ExmoFactory extends GenericFactory {

	ExmoFactory() {
		exchange = Exchanges.EXMO;
		ticker_pub = "EXMO_TICKER_PUB";
		orderbook_pub = "EXMO_ORDERBOOK_PUB";
	}

	public static void exmo(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.exmo_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.EXMO, _ep.getExchange(Exchanges.EXMO).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Exmo_cp = _cp;
				String bscp = Config.getInstance().get(Constants.exmo_currency_pairs);
				if (bscp != null) {
					Exmo_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Exmo_cp.add(new CurrencyPair(pair));
					}
				} else {
					Exmo_cp = _ep.getExchange(Exchanges.EXMO).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Exmo_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.exmo_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getExmoFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Exmo
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.exmo_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getExmoFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Exmo
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.exmo_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getExmoFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}