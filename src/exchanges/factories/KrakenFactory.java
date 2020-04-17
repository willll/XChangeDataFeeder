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

public class KrakenFactory extends GenericStreamingFactory {

	KrakenFactory() {
		exchange = Exchanges.KRAKEN;
		ticker_pub = "KRAKEN_TICKER_PUB";
		orderbook_pub = "KRAKEN_ORDERBOOK_PUB";
	}

	public static void kraken(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.kraken_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.KRAKEN, _ep.getExchange(Exchanges.KRAKEN).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Kraken_cp = _cp;
				String bscp = Config.getInstance().get(Constants.kraken_currency_pairs);
				if (bscp != null) {
					Kraken_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Kraken_cp.add(new CurrencyPair(pair));
					}
				} else {
					Kraken_cp = _ep.getExchange(Exchanges.KRAKEN).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Kraken_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Create a ticker from Kraken
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.kraken_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Kraken
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.kraken_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}