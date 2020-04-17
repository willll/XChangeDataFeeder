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

public class CoinsuperFactory extends GenericFactory {

	CoinsuperFactory() {
		exchange = Exchanges.COINSUPER;
		ticker_pub = "COINSUPER_TICKER_PUB";
		orderbook_pub = "COINSUPER_ORDERBOOK_PUB";
	}

	public static void coinsuper(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinsuper_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.COINSUPER, _ep.getExchange(Exchanges.COINSUPER).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Coinsuper_cp = _cp;
				String bscp = Config.getInstance().get(Constants.coinsuper_currency_pairs);
				if (bscp != null) {
					Coinsuper_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Coinsuper_cp.add(new CurrencyPair(pair));
					}
				} else {
					Coinsuper_cp = _ep.getExchange(Exchanges.COINSUPER).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Coinsuper_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.coinsuper_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getCoinsuperFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Coinsuper
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinsuper_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getCoinsuperFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Coinsuper
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinsuper_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getCoinsuperFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}