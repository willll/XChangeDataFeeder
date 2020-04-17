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

public class BitcoinchartsFactory extends GenericFactory {

	BitcoinchartsFactory() {
		exchange = Exchanges.BITCOINCHARTS;
		ticker_pub = "BITCOINCHARTS_TICKER_PUB";
		orderbook_pub = "BITCOINCHARTS_ORDERBOOK_PUB";
	}

	public static void bitcoincharts(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitcoincharts_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.BITCOINCHARTS, _ep.getExchange(Exchanges.BITCOINCHARTS).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Bitcoincharts_cp = _cp;
				String bscp = Config.getInstance().get(Constants.bitcoincharts_currency_pairs);
				if (bscp != null) {
					Bitcoincharts_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Bitcoincharts_cp.add(new CurrencyPair(pair));
					}
				} else {
					Bitcoincharts_cp = _ep.getExchange(Exchanges.BITCOINCHARTS).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Bitcoincharts_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.bitcoincharts_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getBitcoinchartsFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Bitcoincharts
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitcoincharts_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getBitcoinchartsFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Bitcoincharts
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitcoincharts_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getBitcoinchartsFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}