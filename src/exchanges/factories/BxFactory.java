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

public class BxFactory extends GenericFactory {

	BxFactory() {
		exchange = Exchanges.BX;
		ticker_pub = "BX_TICKER_PUB";
		orderbook_pub = "BX_ORDERBOOK_PUB";
	}

	public static void bx(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bx_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.BX, _ep.getExchange(Exchanges.BX).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Bx_cp = _cp;
				String bscp = Config.getInstance().get(Constants.bx_currency_pairs);
				if (bscp != null) {
					Bx_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Bx_cp.add(new CurrencyPair(pair));
					}
				} else {
					Bx_cp = _ep.getExchange(Exchanges.BX).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Bx_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.bx_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getAcxFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Bx
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bx_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Bx
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bx_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}