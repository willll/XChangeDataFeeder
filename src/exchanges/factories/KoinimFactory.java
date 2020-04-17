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

public class KoinimFactory extends GenericFactory {

	KoinimFactory() {
		exchange = Exchanges.KOINIM;
		ticker_pub = "KOINIM_TICKER_PUB";
		orderbook_pub = "KOINIM_ORDERBOOK_PUB";
	}

	public static void koinim(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.koinim_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.KOINIM, _ep.getExchange(Exchanges.KOINIM).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Koinim_cp = _cp;
				String bscp = Config.getInstance().get(Constants.koinim_currency_pairs);
				if (bscp != null) {
					Koinim_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Koinim_cp.add(new CurrencyPair(pair));
					}
				} else {
					Koinim_cp = _ep.getExchange(Exchanges.KOINIM).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Koinim_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.koinim_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getAcxFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Koinim
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.koinim_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Koinim
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.koinim_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}