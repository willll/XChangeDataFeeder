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

public class CoinfloorFactory extends GenericFactory {

	CoinfloorFactory() {
		exchange = Exchanges.COINFLOOR;
		ticker_pub = "COINFLOOR_TICKER_PUB";
		orderbook_pub = "COINFLOOR_ORDERBOOK_PUB";
	}

	public static void coinfloor(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinfloor_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.COINFLOOR, _ep.getExchange(Exchanges.COINFLOOR).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Coinfloor_cp = _cp;
				String bscp = Config.getInstance().get(Constants.coinfloor_currency_pairs);
				if (bscp != null) {
					Coinfloor_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Coinfloor_cp.add(new CurrencyPair(pair));
					}
				} else {
					Coinfloor_cp = _ep.getExchange(Exchanges.COINFLOOR).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Coinfloor_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.coinfloor_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getCoinfloorFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Coinfloor
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinfloor_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getCoinfloorFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Coinfloor
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinfloor_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getCoinfloorFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}