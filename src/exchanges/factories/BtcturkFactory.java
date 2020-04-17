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

public class BtcturkFactory extends GenericFactory {

	BtcturkFactory() {
		exchange = Exchanges.BTCTURK;
		ticker_pub = "BTCTURK_TICKER_PUB";
		orderbook_pub = "BTCTURK_ORDERBOOK_PUB";
	}

	public static void BTCTurk(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.BTCTurk_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.BTCTURK, _ep.getExchange(Exchanges.BTCTURK).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Btcturk_cp = _cp;
				String bscp = Config.getInstance().get(Constants.BTCTurk_currency_pairs);
				if (bscp != null) {
					Btcturk_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Btcturk_cp.add(new CurrencyPair(pair));
					}
				} else {
					Btcturk_cp = _ep.getExchange(Exchanges.BTCTURK).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Btcturk_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.BTCTurk_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getAcxFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Btcturk
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.BTCTurk_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Btcturk
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.BTCTurk_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}