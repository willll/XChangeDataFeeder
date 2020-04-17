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

public class LykkeFactory extends GenericFactory {

	LykkeFactory() {
		exchange = Exchanges.LYKKE;
		ticker_pub = "LYKKE_TICKER_PUB";
		orderbook_pub = "LYKKE_ORDERBOOK_PUB";
	}

	public static void lykke(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.lykke_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.LYKKE, _ep.getExchange(Exchanges.LYKKE).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Lykke_cp = _cp;
				String bscp = Config.getInstance().get(Constants.lykke_currency_pairs);
				if (bscp != null) {
					Lykke_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Lykke_cp.add(new CurrencyPair(pair));
					}
				} else {
					Lykke_cp = _ep.getExchange(Exchanges.LYKKE).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Lykke_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.lykke_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getAcxFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Lykke
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.lykke_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Lykke
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.lykke_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}