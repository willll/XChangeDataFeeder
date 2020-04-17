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

public class RippleFactory extends GenericFactory {

	RippleFactory() {
		exchange = Exchanges.RIPPLE;
		ticker_pub = "RIPPLE_TICKER_PUB";
		orderbook_pub = "RIPPLE_ORDERBOOK_PUB";
	}

	public static void ripple(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.ripple_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.RIPPLE, _ep.getExchange(Exchanges.RIPPLE).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Ripple_cp = _cp;
				String bscp = Config.getInstance().get(Constants.ripple_currency_pairs);
				if (bscp != null) {
					Ripple_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Ripple_cp.add(new CurrencyPair(pair));
					}
				} else {
					Ripple_cp = _ep.getExchange(Exchanges.RIPPLE).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Ripple_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.ripple_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getAcxFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Ripple
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.ripple_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Ripple
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.ripple_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}