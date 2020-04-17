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

public class LunoFactory extends GenericFactory {

	LunoFactory() {
		exchange = Exchanges.LUNO;
		ticker_pub = "LUNO_TICKER_PUB";
		orderbook_pub = "LUNO_ORDERBOOK_PUB";
	}

	public static void luno(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.luno_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.LUNO, _ep.getExchange(Exchanges.LUNO).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Luno_cp = _cp;
				String bscp = Config.getInstance().get(Constants.luno_currency_pairs);
				if (bscp != null) {
					Luno_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Luno_cp.add(new CurrencyPair(pair));
					}
				} else {
					Luno_cp = _ep.getExchange(Exchanges.LUNO).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Luno_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.luno_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getAcxFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Luno
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.luno_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Luno
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.luno_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}