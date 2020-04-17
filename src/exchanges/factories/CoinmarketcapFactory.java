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

public class CoinmarketcapFactory extends GenericFactory {

	CoinmarketcapFactory() {
		exchange = Exchanges.COINMARKETCAP;
		ticker_pub = "COINMARKETCAP_TICKER_PUB";
		orderbook_pub = "COINMARKETCAP_ORDERBOOK_PUB";
	}

	public static void coinmarketcap(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinmarketcap_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.COINMARKETCAP, _ep.getExchange(Exchanges.COINMARKETCAP).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Coinmarketcap_cp = _cp;
				String bscp = Config.getInstance().get(Constants.coinmarketcap_currency_pairs);
				if (bscp != null) {
					Coinmarketcap_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Coinmarketcap_cp.add(new CurrencyPair(pair));
					}
				} else {
					Coinmarketcap_cp = _ep.getExchange(Exchanges.COINMARKETCAP).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Coinmarketcap_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.coinmarketcap_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getCoinmarketcapFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Coinmarketcap
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinmarketcap_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getCoinmarketcapFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Coinmarketcap
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinmarketcap_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getCoinmarketcapFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}