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

public class CryptonitFactory extends GenericFactory {

	CryptonitFactory() {
		exchange = Exchanges.CRYPTONIT;
		ticker_pub = "CRYPTONIT_TICKER_PUB";
		orderbook_pub = "CRYPTONIT_ORDERBOOK_PUB";
	}

	public static void cryptonit(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.cryptonit_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.CRYPTONIT, _ep.getExchange(Exchanges.CRYPTONIT).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Cryptonit_cp = _cp;
				String bscp = Config.getInstance().get(Constants.cryptonit_currency_pairs);
				if (bscp != null) {
					Cryptonit_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Cryptonit_cp.add(new CurrencyPair(pair));
					}
				} else {
					Cryptonit_cp = _ep.getExchange(Exchanges.CRYPTONIT).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Cryptonit_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.cryptonit_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getCryptonitFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Cryptonit
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.cryptonit_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getCryptonitFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Cryptonit
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.cryptonit_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getCryptonitFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}