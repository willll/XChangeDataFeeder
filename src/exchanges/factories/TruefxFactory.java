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

public class TruefxFactory extends GenericFactory {

	TruefxFactory() {
		exchange = Exchanges.TRUEFX;
		ticker_pub = "TRUEFX_TICKER_PUB";
		orderbook_pub = "TRUEFX_ORDERBOOK_PUB";
	}

	public static void TrueFx(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.TrueFx_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.TRUEFX, _ep.getExchange(Exchanges.TRUEFX).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Truefx_cp = _cp;
				String bscp = Config.getInstance().get(Constants.TrueFx_currency_pairs);
				if (bscp != null) {
					Truefx_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Truefx_cp.add(new CurrencyPair(pair));
					}
				} else {
					Truefx_cp = _ep.getExchange(Exchanges.TRUEFX).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Truefx_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.TrueFx_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getTruefxFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Truefx
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.TrueFx_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getTruefxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Truefx
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.TrueFx_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getTruefxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}