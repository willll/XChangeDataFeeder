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

public class BitmexFactory extends GenericStreamingFactory {

	BitmexFactory() {
		exchange = Exchanges.BITMEX;
		ticker_pub = "BITMEX_TICKER_PUB";
		orderbook_pub = "BITMEX_ORDERBOOK_PUB";
	}

	public static void bitmex(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitmex_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.BITMEX, _ep.getExchange(Exchanges.BITMEX).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Bitmex_cp = _cp;
				String bscp = Config.getInstance().get(Constants.bitmex_currency_pairs);
				if (bscp != null) {
					Bitmex_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Bitmex_cp.add(new CurrencyPair(pair));
					}
				} else {
					Bitmex_cp = _ep.getExchange(Exchanges.BITMEX).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Bitmex_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Create a ticker from Bitmex
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitmex_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getBitmexFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Bitmex
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitmex_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getBitmexFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}