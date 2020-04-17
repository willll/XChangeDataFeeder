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

public class IdexFactory extends GenericFactory {

	IdexFactory() {
		exchange = Exchanges.IDEX;
		ticker_pub = "IDEX_TICKER_PUB";
		orderbook_pub = "IDEX_ORDERBOOK_PUB";
	}

	public static void idex(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.idex_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.IDEX, _ep.getExchange(Exchanges.IDEX).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Idex_cp = _cp;
				String bscp = Config.getInstance().get(Constants.idex_currency_pairs);
				if (bscp != null) {
					Idex_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Idex_cp.add(new CurrencyPair(pair));
					}
				} else {
					Idex_cp = _ep.getExchange(Exchanges.IDEX).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Idex_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.idex_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getAcxFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Idex
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.idex_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Idex
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.idex_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}