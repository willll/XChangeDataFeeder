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

public class CampbxFactory extends GenericFactory {

	CampbxFactory() {
		exchange = Exchanges.CAMPBX;
		ticker_pub = "CAMPBX_TICKER_PUB";
		orderbook_pub = "CAMPBX_ORDERBOOK_PUB";
	}

	public static void CampBX(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.CampBX_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.CAMPBX, _ep.getExchange(Exchanges.CAMPBX).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Campbx_cp = _cp;
				String bscp = Config.getInstance().get(Constants.CampBX_currency_pairs);
				if (bscp != null) {
					Campbx_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Campbx_cp.add(new CurrencyPair(pair));
					}
				} else {
					Campbx_cp = _ep.getExchange(Exchanges.CAMPBX).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Campbx_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.CampBX_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getAcxFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Campbx
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.CampBX_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Campbx
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.CampBX_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}