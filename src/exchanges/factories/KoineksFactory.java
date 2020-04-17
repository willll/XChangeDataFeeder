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

public class KoineksFactory extends GenericFactory {

	KoineksFactory() {
		exchange = Exchanges.KOINEKS;
		ticker_pub = "KOINEKS_TICKER_PUB";
		orderbook_pub = "KOINEKS_ORDERBOOK_PUB";
	}

	public static void koineks(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.koineks_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.KOINEKS, _ep.getExchange(Exchanges.KOINEKS).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Koineks_cp = _cp;
				String bscp = Config.getInstance().get(Constants.koineks_currency_pairs);
				if (bscp != null) {
					Koineks_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Koineks_cp.add(new CurrencyPair(pair));
					}
				} else {
					Koineks_cp = _ep.getExchange(Exchanges.KOINEKS).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Koineks_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.koineks_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getAcxFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Koineks
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.koineks_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Koineks
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.koineks_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}