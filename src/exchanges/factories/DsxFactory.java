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

public class DsxFactory extends GenericFactory {

	DsxFactory() {
		exchange = Exchanges.DSX;
		ticker_pub = "DSX_TICKER_PUB";
		orderbook_pub = "DSX_ORDERBOOK_PUB";
	}

	public static void DSX(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.DSX_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.DSX, _ep.getExchange(Exchanges.DSX).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Dsx_cp = _cp;
				String bscp = Config.getInstance().get(Constants.DSX_currency_pairs);
				if (bscp != null) {
					Dsx_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Dsx_cp.add(new CurrencyPair(pair));
					}
				} else {
					Dsx_cp = _ep.getExchange(Exchanges.DSX).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Dsx_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.DSX_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getAcxFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Dsx
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.DSX_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Dsx
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.DSX_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}