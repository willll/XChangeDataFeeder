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

public class GateioFactory extends GenericFactory {

	GateioFactory() {
		exchange = Exchanges.GATEIO;
		ticker_pub = "GATEIO_TICKER_PUB";
		orderbook_pub = "GATEIO_ORDERBOOK_PUB";
	}

	public static void gateio(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.gateio_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.GATEIO, _ep.getExchange(Exchanges.GATEIO).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Gateio_cp = _cp;
				String bscp = Config.getInstance().get(Constants.gateio_currency_pairs);
				if (bscp != null) {
					Gateio_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Gateio_cp.add(new CurrencyPair(pair));
					}
				} else {
					Gateio_cp = _ep.getExchange(Exchanges.GATEIO).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Gateio_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.gateio_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getAcxFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Gateio
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.gateio_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Gateio
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.gateio_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}