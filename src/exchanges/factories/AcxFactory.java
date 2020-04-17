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

public class AcxFactory extends GenericFactory {

	AcxFactory() {
		exchange = Exchanges.ACX;
		ticker_pub = "ACX_TICKER_PUB";
		orderbook_pub = "ACX_ORDERBOOK_PUB";
	}

	public static void acx(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.acx_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.ACX, _ep.getExchange(Exchanges.ACX).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Acx_cp = _cp;
				String bscp = Config.getInstance().get(Constants.acx_currency_pairs);
				if (bscp != null) {
					Acx_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Acx_cp.add(new CurrencyPair(pair));
					}
				} else {
					Acx_cp = _ep.getExchange(Exchanges.ACX).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Acx_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.acx_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getAcxFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Acx
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.acx_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Acx
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.acx_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}