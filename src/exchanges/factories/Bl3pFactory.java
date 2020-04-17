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

public class Bl3pFactory extends GenericFactory {

	Bl3pFactory() {
		exchange = Exchanges.BL3P;
		ticker_pub = "BL3P_TICKER_PUB";
		orderbook_pub = "BL3P_ORDERBOOK_PUB";
	}

	public static void bl3p(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bl3p_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.BL3P, _ep.getExchange(Exchanges.BL3P).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Bl3p_cp = _cp;
				String bscp = Config.getInstance().get(Constants.bl3p_currency_pairs);
				if (bscp != null) {
					Bl3p_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Bl3p_cp.add(new CurrencyPair(pair));
					}
				} else {
					Bl3p_cp = _ep.getExchange(Exchanges.BL3P).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Bl3p_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.bl3p_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getBl3pFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Bl3p
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bl3p_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getBl3pFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Bl3p
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bl3p_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getBl3pFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}