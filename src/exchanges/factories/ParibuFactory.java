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

public class ParibuFactory extends GenericFactory {

	ParibuFactory() {
		exchange = Exchanges.PARIBU;
		ticker_pub = "PARIBU_TICKER_PUB";
		orderbook_pub = "PARIBU_ORDERBOOK_PUB";
	}

	public static void paribu(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.paribu_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.PARIBU, _ep.getExchange(Exchanges.PARIBU).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Paribu_cp = _cp;
				String bscp = Config.getInstance().get(Constants.paribu_currency_pairs);
				if (bscp != null) {
					Paribu_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Paribu_cp.add(new CurrencyPair(pair));
					}
				} else {
					Paribu_cp = _ep.getExchange(Exchanges.PARIBU).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Paribu_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.paribu_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getAcxFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Paribu
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.paribu_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Paribu
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.paribu_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}