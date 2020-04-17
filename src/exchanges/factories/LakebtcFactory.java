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

public class LakebtcFactory extends GenericFactory {

	LakebtcFactory() {
		exchange = Exchanges.LAKEBTC;
		ticker_pub = "LAKEBTC_TICKER_PUB";
		orderbook_pub = "LAKEBTC_ORDERBOOK_PUB";
	}

	public static void LakeBTC(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.LakeBTC_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.LAKEBTC, _ep.getExchange(Exchanges.LAKEBTC).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Lakebtc_cp = _cp;
				String bscp = Config.getInstance().get(Constants.LakeBTC_currency_pairs);
				if (bscp != null) {
					Lakebtc_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Lakebtc_cp.add(new CurrencyPair(pair));
					}
				} else {
					Lakebtc_cp = _ep.getExchange(Exchanges.LAKEBTC).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Lakebtc_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.LakeBTC_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getLakebtcFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Lakebtc
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.LakeBTC_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getLakebtcFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Lakebtc
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.LakeBTC_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getLakebtcFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}