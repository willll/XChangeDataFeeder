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

public class CoinoneFactory extends GenericFactory {

	CoinoneFactory() {
		exchange = Exchanges.COINONE;
		ticker_pub = "COINONE_TICKER_PUB";
		orderbook_pub = "COINONE_ORDERBOOK_PUB";
	}

	public static void coinone(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinone_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.COINONE, _ep.getExchange(Exchanges.COINONE).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Coinone_cp = _cp;
				String bscp = Config.getInstance().get(Constants.coinone_currency_pairs);
				if (bscp != null) {
					Coinone_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Coinone_cp.add(new CurrencyPair(pair));
					}
				} else {
					Coinone_cp = _ep.getExchange(Exchanges.COINONE).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Coinone_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.coinone_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getCoinoneFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Coinone
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinone_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getCoinoneFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Coinone
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinone_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getCoinoneFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}