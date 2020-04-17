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

public class VaultoroFactory extends GenericFactory {

	VaultoroFactory() {
		exchange = Exchanges.VAULTORO;
		ticker_pub = "VAULTORO_TICKER_PUB";
		orderbook_pub = "VAULTORO_ORDERBOOK_PUB";
	}

	public static void vaultoro(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.vaultoro_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.VAULTORO, _ep.getExchange(Exchanges.VAULTORO).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Vaultoro_cp = _cp;
				String bscp = Config.getInstance().get(Constants.vaultoro_currency_pairs);
				if (bscp != null) {
					Vaultoro_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Vaultoro_cp.add(new CurrencyPair(pair));
					}
				} else {
					Vaultoro_cp = _ep.getExchange(Exchanges.VAULTORO).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Vaultoro_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.vaultoro_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getAcxFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Vaultoro
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.vaultoro_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Vaultoro
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.vaultoro_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}