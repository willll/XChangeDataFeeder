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

public class BitcoindeFactory extends GenericFactory {

	BitcoindeFactory() {
		exchange = Exchanges.BITCOINDE;
		ticker_pub = "BITCOINDE_TICKER_PUB";
		orderbook_pub = "BITCOINDE_ORDERBOOK_PUB";
	}

	public static void bitcoinde(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitcoinde_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.BITCOINDE, _ep.getExchange(Exchanges.BITCOINDE).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Bitcoinde_cp = _cp;
				String bscp = Config.getInstance().get(Constants.bitcoinde_currency_pairs);
				if (bscp != null) {
					Bitcoinde_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Bitcoinde_cp.add(new CurrencyPair(pair));
					}
				} else {
					Bitcoinde_cp = _ep.getExchange(Exchanges.BITCOINDE).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Bitcoinde_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.bitcoinde_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getAcxFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Bitcoinde
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitcoinde_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Bitcoinde
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitcoinde_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}