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

public class BithumbFactory extends GenericFactory {

	BithumbFactory() {
		exchange = Exchanges.BITHUMB;
		ticker_pub = "BITHUMB_TICKER_PUB";
		orderbook_pub = "BITHUMB_ORDERBOOK_PUB";
	}

	public static void bithumb(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bithumb_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.BITHUMB, _ep.getExchange(Exchanges.BITHUMB).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Bithumb_cp = _cp;
				String bscp = Config.getInstance().get(Constants.bithumb_currency_pairs);
				if (bscp != null) {
					Bithumb_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Bithumb_cp.add(new CurrencyPair(pair));
					}
				} else {
					Bithumb_cp = _ep.getExchange(Exchanges.BITHUMB).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Bithumb_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.bithumb_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getBithumbFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Bithumb
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bithumb_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getBithumbFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Bithumb
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bithumb_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getBithumbFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}