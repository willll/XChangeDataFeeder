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

public class CexioFactory extends GenericStreamingFactory {

	CexioFactory() {
		exchange = Exchanges.CEXIO;
		ticker_pub = "CEXIO_TICKER_PUB";
		orderbook_pub = "CEXIO_ORDERBOOK_PUB";
	}

	public static void Cexio(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.Cexio_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.CEXIO, _ep.getExchange(Exchanges.CEXIO).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Cexio_cp = _cp;
				String bscp = Config.getInstance().get(Constants.Cexio_currency_pairs);
				if (bscp != null) {
					Cexio_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Cexio_cp.add(new CurrencyPair(pair));
					}
				} else {
					Cexio_cp = _ep.getExchange(Exchanges.CEXIO).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Cexio_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Create a ticker from Cexio
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.Cexio_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Cexio
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.Cexio_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}