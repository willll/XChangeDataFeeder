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

public class BitfinexFactory extends GenericStreamingFactory {

	BitfinexFactory() {
		exchange = Exchanges.BITFINEX;
		ticker_pub = "BITFINEX_TICKER_PUB";
		orderbook_pub = "BITFINEX_ORDERBOOK_PUB";
	}

	public static void bitfinex(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitfinex_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.BITFINEX, _ep.getExchange(Exchanges.BITFINEX).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Bitfinex_cp = _cp;
				String bscp = Config.getInstance().get(Constants.bitfinex_currency_pairs);
				if (bscp != null) {
					Bitfinex_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Bitfinex_cp.add(new CurrencyPair(pair));
					}
				} else {
					Bitfinex_cp = _ep.getExchange(Exchanges.BITFINEX).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Bitfinex_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Create a ticker from Bitfinex
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitfinex_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getBitfinexFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Bitfinex
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitfinex_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getBitfinexFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}