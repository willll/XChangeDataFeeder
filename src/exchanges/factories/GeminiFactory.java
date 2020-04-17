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

public class GeminiFactory extends GenericStreamingFactory {

	GeminiFactory() {
		exchange = Exchanges.GEMINI;
		ticker_pub = "GEMINI_TICKER_PUB";
		orderbook_pub = "GEMINI_ORDERBOOK_PUB";
	}

	public static void gemini(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.gemini_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.GEMINI, _ep.getExchange(Exchanges.GEMINI).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Gemini_cp = _cp;
				String bscp = Config.getInstance().get(Constants.gemini_currency_pairs);
				if (bscp != null) {
					Gemini_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Gemini_cp.add(new CurrencyPair(pair));
					}
				} else {
					Gemini_cp = _ep.getExchange(Exchanges.GEMINI).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Gemini_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Create a ticker from Gemini
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.gemini_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Gemini
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.gemini_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}