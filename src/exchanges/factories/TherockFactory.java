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

public class TherockFactory extends GenericFactory {

	TherockFactory() {
		exchange = Exchanges.THEROCK;
		ticker_pub = "THEROCK_TICKER_PUB";
		orderbook_pub = "THEROCK_ORDERBOOK_PUB";
	}

	public static void TheRock(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.TheRock_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.THEROCK, _ep.getExchange(Exchanges.THEROCK).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Therock_cp = _cp;
				String bscp = Config.getInstance().get(Constants.TheRock_currency_pairs);
				if (bscp != null) {
					Therock_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Therock_cp.add(new CurrencyPair(pair));
					}
				} else {
					Therock_cp = _ep.getExchange(Exchanges.THEROCK).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Therock_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.TheRock_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getAcxFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Therock
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.TheRock_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Therock
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.TheRock_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}