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

public class BitbayFactory extends GenericFactory {

	BitbayFactory() {
		exchange = Exchanges.BITBAY;
		ticker_pub = "BITBAY_TICKER_PUB";
		orderbook_pub = "BITBAY_ORDERBOOK_PUB";
	}

	public static void bitbay(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitbay_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.BITBAY, _ep.getExchange(Exchanges.BITBAY).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Bitbay_cp = _cp;
				String bscp = Config.getInstance().get(Constants.bitbay_currency_pairs);
				if (bscp != null) {
					Bitbay_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Bitbay_cp.add(new CurrencyPair(pair));
					}
				} else {
					Bitbay_cp = _ep.getExchange(Exchanges.BITBAY).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Bitbay_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.bitbay_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getAcxFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Bitbay
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitbay_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Bitbay
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitbay_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}