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

public class BityFactory extends GenericFactory {

	BityFactory() {
		exchange = Exchanges.BITY;
		ticker_pub = "BITY_TICKER_PUB";
		orderbook_pub = "BITY_ORDERBOOK_PUB";
	}

	public static void bity(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bity_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.BITY, _ep.getExchange(Exchanges.BITY).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Bity_cp = _cp;
				String bscp = Config.getInstance().get(Constants.bity_currency_pairs);
				if (bscp != null) {
					Bity_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Bity_cp.add(new CurrencyPair(pair));
					}
				} else {
					Bity_cp = _ep.getExchange(Exchanges.BITY).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Bity_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.bity_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getBityFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Bity
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bity_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getBityFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Bity
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bity_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getBityFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}