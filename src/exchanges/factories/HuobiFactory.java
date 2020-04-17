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

public class HuobiFactory extends GenericFactory {

	HuobiFactory() {
		exchange = Exchanges.HUOBI;
		ticker_pub = "HUOBI_TICKER_PUB";
		orderbook_pub = "HUOBI_ORDERBOOK_PUB";
	}

	public static void huobi(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.huobi_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.HUOBI, _ep.getExchange(Exchanges.HUOBI).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Huobi_cp = _cp;
				String bscp = Config.getInstance().get(Constants.huobi_currency_pairs);
				if (bscp != null) {
					Huobi_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Huobi_cp.add(new CurrencyPair(pair));
					}
				} else {
					Huobi_cp = _ep.getExchange(Exchanges.HUOBI).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Huobi_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.huobi_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getAcxFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Huobi
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.huobi_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Huobi
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.huobi_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}