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

public class KunaFactory extends GenericFactory {

	KunaFactory() {
		exchange = Exchanges.KUNA;
		ticker_pub = "KUNA_TICKER_PUB";
		orderbook_pub = "KUNA_ORDERBOOK_PUB";
	}

	public static void kuna(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.kuna_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.KUNA, _ep.getExchange(Exchanges.KUNA).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Kuna_cp = _cp;
				String bscp = Config.getInstance().get(Constants.kuna_currency_pairs);
				if (bscp != null) {
					Kuna_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Kuna_cp.add(new CurrencyPair(pair));
					}
				} else {
					Kuna_cp = _ep.getExchange(Exchanges.KUNA).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Kuna_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.kuna_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getKunaFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Kuna
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.kuna_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getKunaFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Kuna
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.kuna_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getKunaFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}