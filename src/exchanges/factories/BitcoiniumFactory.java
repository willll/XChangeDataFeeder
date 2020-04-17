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

public class BitcoiniumFactory extends GenericFactory {

	BitcoiniumFactory() {
		exchange = Exchanges.BITCOINIUM;
		ticker_pub = "BITCOINIUM_TICKER_PUB";
		orderbook_pub = "BITCOINIUM_ORDERBOOK_PUB";
	}

	public static void bitcoinium(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitcoinium_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.BITCOINIUM, _ep.getExchange(Exchanges.BITCOINIUM).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Bitcoinium_cp = _cp;
				String bscp = Config.getInstance().get(Constants.bitcoinium_currency_pairs);
				if (bscp != null) {
					Bitcoinium_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Bitcoinium_cp.add(new CurrencyPair(pair));
					}
				} else {
					Bitcoinium_cp = _ep.getExchange(Exchanges.BITCOINIUM).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Bitcoinium_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.bitcoinium_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getAcxFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Bitcoinium
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitcoinium_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Bitcoinium
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitcoinium_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}