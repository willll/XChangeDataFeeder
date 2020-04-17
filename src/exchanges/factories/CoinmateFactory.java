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

public class CoinmateFactory extends GenericStreamingFactory {

	CoinmateFactory() {
		exchange = Exchanges.COINMATE;
		ticker_pub = "COINMATE_TICKER_PUB";
		orderbook_pub = "COINMATE_ORDERBOOK_PUB";
	}

	public static void coinmate(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinmate_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.COINMATE, _ep.getExchange(Exchanges.COINMATE).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Coinmate_cp = _cp;
				String bscp = Config.getInstance().get(Constants.coinmate_currency_pairs);
				if (bscp != null) {
					Coinmate_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Coinmate_cp.add(new CurrencyPair(pair));
					}
				} else {
					Coinmate_cp = _ep.getExchange(Exchanges.COINMATE).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Coinmate_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Create a ticker from Coinmate
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinmate_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getCoinmateFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Coinmate
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinmate_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getCoinmateFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}