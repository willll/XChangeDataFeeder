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

public class ExxFactory extends GenericFactory {

	ExxFactory() {
		exchange = Exchanges.EXX;
		ticker_pub = "EXX_TICKER_PUB";
		orderbook_pub = "EXX_ORDERBOOK_PUB";
	}

	public static void EXX(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.EXX_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.EXX, _ep.getExchange(Exchanges.EXX).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Exx_cp = _cp;
				String bscp = Config.getInstance().get(Constants.EXX_currency_pairs);
				if (bscp != null) {
					Exx_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Exx_cp.add(new CurrencyPair(pair));
					}
				} else {
					Exx_cp = _ep.getExchange(Exchanges.EXX).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Exx_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.EXX_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getExxFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Exx
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.EXX_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getExxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Exx
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.EXX_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getExxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}