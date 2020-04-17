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

public class BitzFactory extends GenericFactory {

	BitzFactory() {
		exchange = Exchanges.BITZ;
		ticker_pub = "BITZ_TICKER_PUB";
		orderbook_pub = "BITZ_ORDERBOOK_PUB";
	}

	public static void bitZ(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitZ_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.BITZ, _ep.getExchange(Exchanges.BITZ).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Bitz_cp = _cp;
				String bscp = Config.getInstance().get(Constants.bitZ_currency_pairs);
				if (bscp != null) {
					Bitz_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Bitz_cp.add(new CurrencyPair(pair));
					}
				} else {
					Bitz_cp = _ep.getExchange(Exchanges.BITZ).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Bitz_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.bitZ_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getBitzFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Bitz
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitZ_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getBitzFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Bitz
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitZ_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getBitzFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}