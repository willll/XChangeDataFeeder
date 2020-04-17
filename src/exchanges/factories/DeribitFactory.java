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

public class DeribitFactory extends GenericFactory {

	DeribitFactory() {
		exchange = Exchanges.DERIBIT;
		ticker_pub = "DERIBIT_TICKER_PUB";
		orderbook_pub = "DERIBIT_ORDERBOOK_PUB";
	}

	public static void deribit(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.deribit_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.DERIBIT, _ep.getExchange(Exchanges.DERIBIT).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Deribit_cp = _cp;
				String bscp = Config.getInstance().get(Constants.deribit_currency_pairs);
				if (bscp != null) {
					Deribit_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Deribit_cp.add(new CurrencyPair(pair));
					}
				} else {
					Deribit_cp = _ep.getExchange(Exchanges.DERIBIT).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Deribit_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.deribit_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getDeribitFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Deribit
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.deribit_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getDeribitFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Deribit
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.deribit_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getDeribitFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}