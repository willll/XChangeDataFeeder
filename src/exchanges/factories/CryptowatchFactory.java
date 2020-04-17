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

public class CryptowatchFactory extends GenericFactory {

	CryptowatchFactory() {
		exchange = Exchanges.CRYPTOWATCH;
		ticker_pub = "CRYPTOWATCH_TICKER_PUB";
		orderbook_pub = "CRYPTOWATCH_ORDERBOOK_PUB";
	}

	public static void cryptowatch(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.cryptowatch_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.CRYPTOWATCH, _ep.getExchange(Exchanges.CRYPTOWATCH).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Cryptowatch_cp = _cp;
				String bscp = Config.getInstance().get(Constants.cryptowatch_currency_pairs);
				if (bscp != null) {
					Cryptowatch_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Cryptowatch_cp.add(new CurrencyPair(pair));
					}
				} else {
					Cryptowatch_cp = _ep.getExchange(Exchanges.CRYPTOWATCH).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Cryptowatch_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.cryptowatch_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getCryptowatchFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Cryptowatch
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.cryptowatch_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getCryptowatchFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Cryptowatch
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.cryptowatch_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getCryptowatchFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}