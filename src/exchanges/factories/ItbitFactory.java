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

public class ItbitFactory extends GenericFactory {

	ItbitFactory() {
		exchange = Exchanges.ITBIT;
		ticker_pub = "ITBIT_TICKER_PUB";
		orderbook_pub = "ITBIT_ORDERBOOK_PUB";
	}

	public static void ItBit(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.ItBit_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.ITBIT, _ep.getExchange(Exchanges.ITBIT).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Itbit_cp = _cp;
				String bscp = Config.getInstance().get(Constants.ItBit_currency_pairs);
				if (bscp != null) {
					Itbit_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Itbit_cp.add(new CurrencyPair(pair));
					}
				} else {
					Itbit_cp = _ep.getExchange(Exchanges.ITBIT).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Itbit_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.ItBit_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getAcxFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Itbit
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.ItBit_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Itbit
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.ItBit_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}