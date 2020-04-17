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

public class BitsoFactory extends GenericFactory {

	BitsoFactory() {
		exchange = Exchanges.BITSO;
		ticker_pub = "BITSO_TICKER_PUB";
		orderbook_pub = "BITSO_ORDERBOOK_PUB";
	}

	public static void bitso(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitso_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.BITSO, _ep.getExchange(Exchanges.BITSO).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Bitso_cp = _cp;
				String bscp = Config.getInstance().get(Constants.bitso_currency_pairs);
				if (bscp != null) {
					Bitso_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Bitso_cp.add(new CurrencyPair(pair));
					}
				} else {
					Bitso_cp = _ep.getExchange(Exchanges.BITSO).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Bitso_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.bitso_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getAcxFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Bitso
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitso_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Bitso
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitso_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}