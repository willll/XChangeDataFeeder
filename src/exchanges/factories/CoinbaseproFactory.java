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

public class CoinbaseproFactory extends GenericStreamingFactory {

	CoinbaseproFactory() {
		exchange = Exchanges.COINBASEPRO;
		ticker_pub = "COINBASEPRO_TICKER_PUB";
		orderbook_pub = "COINBASEPRO_ORDERBOOK_PUB";
	}

	public static void coinbasepro(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinbasepro_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.COINBASEPRO, _ep.getExchange(Exchanges.COINBASEPRO).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Coinbasepro_cp = _cp;
				String bscp = Config.getInstance().get(Constants.coinbasepro_currency_pairs);
				if (bscp != null) {
					Coinbasepro_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Coinbasepro_cp.add(new CurrencyPair(pair));
					}
				} else {
					Coinbasepro_cp = _ep.getExchange(Exchanges.COINBASEPRO).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Coinbasepro_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Create a ticker from Coinbasepro
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinbasepro_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Coinbasepro
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinbasepro_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}