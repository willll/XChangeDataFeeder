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

public class LatokenFactory extends GenericFactory {

	LatokenFactory() {
		exchange = Exchanges.LATOKEN;
		ticker_pub = "LATOKEN_TICKER_PUB";
		orderbook_pub = "LATOKEN_ORDERBOOK_PUB";
	}

	public static void latoken(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.latoken_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.LATOKEN, _ep.getExchange(Exchanges.LATOKEN).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Latoken_cp = _cp;
				String bscp = Config.getInstance().get(Constants.latoken_currency_pairs);
				if (bscp != null) {
					Latoken_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Latoken_cp.add(new CurrencyPair(pair));
					}
				} else {
					Latoken_cp = _ep.getExchange(Exchanges.LATOKEN).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Latoken_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.latoken_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getLatokenFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Latoken
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.latoken_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getLatokenFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Latoken
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.latoken_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getLatokenFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}