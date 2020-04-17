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

public class KucoinFactory extends GenericFactory {

	KucoinFactory() {
		exchange = Exchanges.KUCOIN;
		ticker_pub = "KUCOIN_TICKER_PUB";
		orderbook_pub = "KUCOIN_ORDERBOOK_PUB";
	}

	public static void kucoin(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.kucoin_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.KUCOIN, _ep.getExchange(Exchanges.KUCOIN).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Kucoin_cp = _cp;
				String bscp = Config.getInstance().get(Constants.kucoin_currency_pairs);
				if (bscp != null) {
					Kucoin_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Kucoin_cp.add(new CurrencyPair(pair));
					}
				} else {
					Kucoin_cp = _ep.getExchange(Exchanges.KUCOIN).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Kucoin_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.kucoin_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getAcxFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Kucoin
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.kucoin_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Kucoin
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.kucoin_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}