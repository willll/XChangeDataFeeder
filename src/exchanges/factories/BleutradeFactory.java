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

public class BleutradeFactory extends GenericFactory {

	BleutradeFactory() {
		exchange = Exchanges.BLEUTRADE;
		ticker_pub = "BLEUTRADE_TICKER_PUB";
		orderbook_pub = "BLEUTRADE_ORDERBOOK_PUB";
	}

	public static void bleutrade(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bleutrade_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.BLEUTRADE, _ep.getExchange(Exchanges.BLEUTRADE).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Bleutrade_cp = _cp;
				String bscp = Config.getInstance().get(Constants.bleutrade_currency_pairs);
				if (bscp != null) {
					Bleutrade_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Bleutrade_cp.add(new CurrencyPair(pair));
					}
				} else {
					Bleutrade_cp = _ep.getExchange(Exchanges.BLEUTRADE).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Bleutrade_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.bleutrade_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getAcxFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Bleutrade
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bleutrade_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Bleutrade
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bleutrade_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}