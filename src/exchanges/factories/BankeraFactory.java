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

public class BankeraFactory extends GenericStreamingFactory {

	BankeraFactory() {
		exchange = Exchanges.BANKERA;
		ticker_pub = "BANKERA_TICKER_PUB";
		orderbook_pub = "BANKERA_ORDERBOOK_PUB";
	}

	public static void bankera(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bankera_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.BANKERA, _ep.getExchange(Exchanges.BANKERA).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Bankera_cp = _cp;
				String bscp = Config.getInstance().get(Constants.bankera_currency_pairs);
				if (bscp != null) {
					Bankera_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Bankera_cp.add(new CurrencyPair(pair));
					}
				} else {
					Bankera_cp = _ep.getExchange(Exchanges.BANKERA).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Bankera_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Create a ticker from Bankera
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bankera_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Bankera
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bankera_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}