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

public class YobitFactory extends GenericFactory {

	YobitFactory() {
		exchange = Exchanges.YOBIT;
		ticker_pub = "YOBIT_TICKER_PUB";
		orderbook_pub = "YOBIT_ORDERBOOK_PUB";
	}

	public static void YoBit(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.YoBit_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.YOBIT, _ep.getExchange(Exchanges.YOBIT).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Yobit_cp = _cp;
				String bscp = Config.getInstance().get(Constants.YoBit_currency_pairs);
				if (bscp != null) {
					Yobit_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Yobit_cp.add(new CurrencyPair(pair));
					}
				} else {
					Yobit_cp = _ep.getExchange(Exchanges.YOBIT).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Yobit_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.YoBit_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getYobitFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Yobit
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.YoBit_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getYobitFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Yobit
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.YoBit_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getYobitFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}