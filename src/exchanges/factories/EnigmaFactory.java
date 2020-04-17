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

public class EnigmaFactory extends GenericFactory {

	EnigmaFactory() {
		exchange = Exchanges.ENIGMA;
		ticker_pub = "ENIGMA_TICKER_PUB";
		orderbook_pub = "ENIGMA_ORDERBOOK_PUB";
	}

	public static void enigma(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.enigma_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.ENIGMA, _ep.getExchange(Exchanges.ENIGMA).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Enigma_cp = _cp;
				String bscp = Config.getInstance().get(Constants.enigma_currency_pairs);
				if (bscp != null) {
					Enigma_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Enigma_cp.add(new CurrencyPair(pair));
					}
				} else {
					Enigma_cp = _ep.getExchange(Exchanges.ENIGMA).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Enigma_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.enigma_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getEnigmaFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Enigma
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.enigma_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getEnigmaFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Enigma
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.enigma_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getEnigmaFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}