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

public class PaymiumFactory extends GenericFactory {

	PaymiumFactory() {
		exchange = Exchanges.PAYMIUM;
		ticker_pub = "PAYMIUM_TICKER_PUB";
		orderbook_pub = "PAYMIUM_ORDERBOOK_PUB";
	}

	public static void paymium(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.paymium_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.PAYMIUM, _ep.getExchange(Exchanges.PAYMIUM).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Paymium_cp = _cp;
				String bscp = Config.getInstance().get(Constants.paymium_currency_pairs);
				if (bscp != null) {
					Paymium_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Paymium_cp.add(new CurrencyPair(pair));
					}
				} else {
					Paymium_cp = _ep.getExchange(Exchanges.PAYMIUM).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Paymium_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.paymium_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getAcxFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Paymium
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.paymium_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Paymium
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.paymium_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}