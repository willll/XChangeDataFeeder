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

public class BitcoincoreFactory extends GenericFactory {

	BitcoincoreFactory() {
		exchange = Exchanges.BITCOINCORE;
		ticker_pub = "BITCOINCORE_TICKER_PUB";
		orderbook_pub = "BITCOINCORE_ORDERBOOK_PUB";
	}

	public static void bitcoincore(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitcoincore_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.BITCOINCORE, _ep.getExchange(Exchanges.BITCOINCORE).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Bitcoincore_cp = _cp;
				String bscp = Config.getInstance().get(Constants.bitcoincore_currency_pairs);
				if (bscp != null) {
					Bitcoincore_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Bitcoincore_cp.add(new CurrencyPair(pair));
					}
				} else {
					Bitcoincore_cp = _ep.getExchange(Exchanges.BITCOINCORE).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Bitcoincore_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.bitcoincore_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getAcxFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Bitcoincore
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitcoincore_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Bitcoincore
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitcoincore_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}