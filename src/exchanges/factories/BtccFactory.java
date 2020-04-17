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

public class BtccFactory extends GenericFactory {

	BtccFactory() {
		exchange = Exchanges.BTCC;
		ticker_pub = "BTCC_TICKER_PUB";
		orderbook_pub = "BTCC_ORDERBOOK_PUB";
	}

	public static void btcc(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.btcc_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.BTCC, _ep.getExchange(Exchanges.BTCC).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Btcc_cp = _cp;
				String bscp = Config.getInstance().get(Constants.btcc_currency_pairs);
				if (bscp != null) {
					Btcc_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Btcc_cp.add(new CurrencyPair(pair));
					}
				} else {
					Btcc_cp = _ep.getExchange(Exchanges.BTCC).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Btcc_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.btcc_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getBtccFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Btcc
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.btcc_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getBtccFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Btcc
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.btcc_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getBtccFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}