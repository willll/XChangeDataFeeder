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

public class CryptofacilitiesFactory extends GenericFactory {

	CryptofacilitiesFactory() {
		exchange = Exchanges.CRYPTOFACILITIES;
		ticker_pub = "CRYPTOFACILITIES_TICKER_PUB";
		orderbook_pub = "CRYPTOFACILITIES_ORDERBOOK_PUB";
	}

	public static void CryptoFacilities(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.CryptoFacilities_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.CRYPTOFACILITIES, _ep.getExchange(Exchanges.CRYPTOFACILITIES).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Cryptofacilities_cp = _cp;
				String bscp = Config.getInstance().get(Constants.CryptoFacilities_currency_pairs);
				if (bscp != null) {
					Cryptofacilities_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Cryptofacilities_cp.add(new CurrencyPair(pair));
					}
				} else {
					Cryptofacilities_cp = _ep.getExchange(Exchanges.CRYPTOFACILITIES).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Cryptofacilities_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.CryptoFacilities_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getAcxFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Cryptofacilities
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.CryptoFacilities_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Cryptofacilities
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.CryptoFacilities_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}