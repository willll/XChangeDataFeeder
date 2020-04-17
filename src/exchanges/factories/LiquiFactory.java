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

public class LiquiFactory extends GenericFactory {

	LiquiFactory() {
		exchange = Exchanges.LIQUI;
		ticker_pub = "LIQUI_TICKER_PUB";
		orderbook_pub = "LIQUI_ORDERBOOK_PUB";
	}

	public static void liqui(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.liqui_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.LIQUI, _ep.getExchange(Exchanges.LIQUI).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Liqui_cp = _cp;
				String bscp = Config.getInstance().get(Constants.liqui_currency_pairs);
				if (bscp != null) {
					Liqui_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Liqui_cp.add(new CurrencyPair(pair));
					}
				} else {
					Liqui_cp = _ep.getExchange(Exchanges.LIQUI).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Liqui_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.liqui_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getAcxFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Liqui
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.liqui_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Liqui
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.liqui_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}