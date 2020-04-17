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

public class CoinexFactory extends GenericFactory {

	CoinexFactory() {
		exchange = Exchanges.COINEX;
		ticker_pub = "COINEX_TICKER_PUB";
		orderbook_pub = "COINEX_ORDERBOOK_PUB";
	}

	public static void coinex(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinex_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.COINEX, _ep.getExchange(Exchanges.COINEX).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Coinex_cp = _cp;
				String bscp = Config.getInstance().get(Constants.coinex_currency_pairs);
				if (bscp != null) {
					Coinex_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Coinex_cp.add(new CurrencyPair(pair));
					}
				} else {
					Coinex_cp = _ep.getExchange(Exchanges.COINEX).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Coinex_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.coinex_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getCoinexFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Coinex
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinex_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getCoinexFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Coinex
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinex_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getCoinexFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}