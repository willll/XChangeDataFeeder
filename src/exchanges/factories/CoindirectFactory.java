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

public class CoindirectFactory extends GenericFactory {

	CoindirectFactory() {
		exchange = Exchanges.COINDIRECT;
		ticker_pub = "COINDIRECT_TICKER_PUB";
		orderbook_pub = "COINDIRECT_ORDERBOOK_PUB";
	}

	public static void coindirect(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.coindirect_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.COINDIRECT, _ep.getExchange(Exchanges.COINDIRECT).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Coindirect_cp = _cp;
				String bscp = Config.getInstance().get(Constants.coindirect_currency_pairs);
				if (bscp != null) {
					Coindirect_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Coindirect_cp.add(new CurrencyPair(pair));
					}
				} else {
					Coindirect_cp = _ep.getExchange(Exchanges.COINDIRECT).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Coindirect_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.coindirect_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getAcxFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Coindirect
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.coindirect_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Coindirect
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.coindirect_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}