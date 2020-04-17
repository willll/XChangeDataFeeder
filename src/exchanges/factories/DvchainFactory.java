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

public class DvchainFactory extends GenericFactory {

	DvchainFactory() {
		exchange = Exchanges.DVCHAIN;
		ticker_pub = "DVCHAIN_TICKER_PUB";
		orderbook_pub = "DVCHAIN_ORDERBOOK_PUB";
	}

	public static void DVChain(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.DVChain_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.DVCHAIN, _ep.getExchange(Exchanges.DVCHAIN).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Dvchain_cp = _cp;
				String bscp = Config.getInstance().get(Constants.DVChain_currency_pairs);
				if (bscp != null) {
					Dvchain_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Dvchain_cp.add(new CurrencyPair(pair));
					}
				} else {
					Dvchain_cp = _ep.getExchange(Exchanges.DVCHAIN).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Dvchain_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.DVChain_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getAcxFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Dvchain
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.DVChain_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Dvchain
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.DVChain_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}