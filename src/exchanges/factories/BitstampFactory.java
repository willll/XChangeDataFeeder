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

public class BitstampFactory extends GenericStreamingFactory {

	BitstampFactory() {
		exchange = Exchanges.BITSTAMP;
		ticker_pub = "BITSTAMP_TICKER_PUB";
		orderbook_pub = "BITSTAMP_ORDERBOOK_PUB";
	}

	public static void bitstamp(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitstamp_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.BITSTAMP, _ep.getExchange(Exchanges.BITSTAMP).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Bitstamp_cp = _cp;
				String bscp = Config.getInstance().get(Constants.bitstamp_currency_pairs);
				if (bscp != null) {
					Bitstamp_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Bitstamp_cp.add(new CurrencyPair(pair));
					}
				} else {
					Bitstamp_cp = _ep.getExchange(Exchanges.BITSTAMP).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Bitstamp_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Create a ticker from Bitstamp
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitstamp_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getBitstampFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Bitstamp
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitstamp_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getBitstampFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}