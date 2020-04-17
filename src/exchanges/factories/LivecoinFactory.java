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

public class LivecoinFactory extends GenericFactory {

	LivecoinFactory() {
		exchange = Exchanges.LIVECOIN;
		ticker_pub = "LIVECOIN_TICKER_PUB";
		orderbook_pub = "LIVECOIN_ORDERBOOK_PUB";
	}

	public static void livecoin(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.livecoin_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.LIVECOIN, _ep.getExchange(Exchanges.LIVECOIN).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Livecoin_cp = _cp;
				String bscp = Config.getInstance().get(Constants.livecoin_currency_pairs);
				if (bscp != null) {
					Livecoin_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Livecoin_cp.add(new CurrencyPair(pair));
					}
				} else {
					Livecoin_cp = _ep.getExchange(Exchanges.LIVECOIN).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Livecoin_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.livecoin_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getLivecoinFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Livecoin
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.livecoin_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getLivecoinFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Livecoin
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.livecoin_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getLivecoinFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}