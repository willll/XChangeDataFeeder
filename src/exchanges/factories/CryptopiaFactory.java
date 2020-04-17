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

public class CryptopiaFactory extends GenericFactory {

	CryptopiaFactory() {
		exchange = Exchanges.CRYPTOPIA;
		ticker_pub = "CRYPTOPIA_TICKER_PUB";
		orderbook_pub = "CRYPTOPIA_ORDERBOOK_PUB";
	}

	public static void cryptopia(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.cryptopia_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.CRYPTOPIA, _ep.getExchange(Exchanges.CRYPTOPIA).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Cryptopia_cp = _cp;
				String bscp = Config.getInstance().get(Constants.cryptopia_currency_pairs);
				if (bscp != null) {
					Cryptopia_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Cryptopia_cp.add(new CurrencyPair(pair));
					}
				} else {
					Cryptopia_cp = _ep.getExchange(Exchanges.CRYPTOPIA).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Cryptopia_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.cryptopia_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getCryptopiaFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Cryptopia
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.cryptopia_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getCryptopiaFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Cryptopia
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.cryptopia_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getCryptopiaFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}