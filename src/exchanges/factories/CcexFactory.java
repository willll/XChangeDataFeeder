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

public class CcexFactory extends GenericFactory {

	CcexFactory() {
		exchange = Exchanges.CCEX;
		ticker_pub = "CCEX_TICKER_PUB";
		orderbook_pub = "CCEX_ORDERBOOK_PUB";
	}

	public static void CCEX(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.CCEX_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.CCEX, _ep.getExchange(Exchanges.CCEX).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Ccex_cp = _cp;
				String bscp = Config.getInstance().get(Constants.CCEX_currency_pairs);
				if (bscp != null) {
					Ccex_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Ccex_cp.add(new CurrencyPair(pair));
					}
				} else {
					Ccex_cp = _ep.getExchange(Exchanges.CCEX).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Ccex_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.CCEX_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getCcexFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Ccex
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.CCEX_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getCcexFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Ccex
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.CCEX_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getCcexFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}