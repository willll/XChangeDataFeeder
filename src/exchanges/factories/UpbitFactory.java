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

public class UpbitFactory extends GenericFactory {

	UpbitFactory() {
		exchange = Exchanges.UPBIT;
		ticker_pub = "UPBIT_TICKER_PUB";
		orderbook_pub = "UPBIT_ORDERBOOK_PUB";
	}

	public static void upbit(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.upbit_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.UPBIT, _ep.getExchange(Exchanges.UPBIT).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Upbit_cp = _cp;
				String bscp = Config.getInstance().get(Constants.upbit_currency_pairs);
				if (bscp != null) {
					Upbit_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Upbit_cp.add(new CurrencyPair(pair));
					}
				} else {
					Upbit_cp = _ep.getExchange(Exchanges.UPBIT).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Upbit_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.upbit_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getAcxFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Upbit
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.upbit_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Upbit
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.upbit_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}