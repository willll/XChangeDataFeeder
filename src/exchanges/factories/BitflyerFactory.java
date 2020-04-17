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

public class BitflyerFactory extends GenericStreamingFactory {

	BitflyerFactory() {
		exchange = Exchanges.BITFLYER;
		ticker_pub = "BITFLYER_TICKER_PUB";
		orderbook_pub = "BITFLYER_ORDERBOOK_PUB";
	}

	public static void bitflyer(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitflyer_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.BITFLYER, _ep.getExchange(Exchanges.BITFLYER).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Bitflyer_cp = _cp;
				String bscp = Config.getInstance().get(Constants.bitflyer_currency_pairs);
				if (bscp != null) {
					Bitflyer_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Bitflyer_cp.add(new CurrencyPair(pair));
					}
				} else {
					Bitflyer_cp = _ep.getExchange(Exchanges.BITFLYER).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Bitflyer_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Create a ticker from Bitflyer
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitflyer_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getBitflyerFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Bitflyer
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitflyer_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getBitflyerFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}