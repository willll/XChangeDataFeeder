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

public class CoinbaseFactory extends GenericFactory {

	CoinbaseFactory() {
		exchange = Exchanges.COINBASE;
		ticker_pub = "COINBASE_TICKER_PUB";
		orderbook_pub = "COINBASE_ORDERBOOK_PUB";
	}

	public static void coinbase(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinbase_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.COINBASE, _ep.getExchange(Exchanges.COINBASE).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Coinbase_cp = _cp;
				String bscp = Config.getInstance().get(Constants.coinbase_currency_pairs);
				if (bscp != null) {
					Coinbase_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Coinbase_cp.add(new CurrencyPair(pair));
					}
				} else {
					Coinbase_cp = _ep.getExchange(Exchanges.COINBASE).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Coinbase_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.coinbase_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getAcxFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Coinbase
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinbase_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Coinbase
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinbase_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}