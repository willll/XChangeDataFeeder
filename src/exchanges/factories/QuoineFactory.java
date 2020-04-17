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

public class QuoineFactory extends GenericFactory {

	QuoineFactory() {
		exchange = Exchanges.QUOINE;
		ticker_pub = "QUOINE_TICKER_PUB";
		orderbook_pub = "QUOINE_ORDERBOOK_PUB";
	}

	public static void quoine(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.quoine_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.QUOINE, _ep.getExchange(Exchanges.QUOINE).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Quoine_cp = _cp;
				String bscp = Config.getInstance().get(Constants.quoine_currency_pairs);
				if (bscp != null) {
					Quoine_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Quoine_cp.add(new CurrencyPair(pair));
					}
				} else {
					Quoine_cp = _ep.getExchange(Exchanges.QUOINE).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Quoine_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.quoine_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getAcxFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Quoine
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.quoine_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Quoine
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.quoine_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}