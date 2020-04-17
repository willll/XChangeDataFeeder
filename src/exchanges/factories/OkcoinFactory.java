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

public class OkcoinFactory extends GenericStreamingFactory {

	OkcoinFactory() {
		exchange = Exchanges.OKCOIN;
		ticker_pub = "OKCOIN_TICKER_PUB";
		orderbook_pub = "OKCOIN_ORDERBOOK_PUB";
	}

	public static void OkCoin(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.OkCoin_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.OKCOIN, _ep.getExchange(Exchanges.OKCOIN).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Okcoin_cp = _cp;
				String bscp = Config.getInstance().get(Constants.OkCoin_currency_pairs);
				if (bscp != null) {
					Okcoin_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Okcoin_cp.add(new CurrencyPair(pair));
					}
				} else {
					Okcoin_cp = _ep.getExchange(Exchanges.OKCOIN).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Okcoin_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Create a ticker from Okcoin
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.OkCoin_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Okcoin
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.OkCoin_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}