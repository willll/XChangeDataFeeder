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

public class BtctradeFactory extends GenericFactory {

	BtctradeFactory() {
		exchange = Exchanges.BTCTRADE;
		ticker_pub = "BTCTRADE_TICKER_PUB";
		orderbook_pub = "BTCTRADE_ORDERBOOK_PUB";
	}

	public static void BTCTrade(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.BTCTrade_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.BTCTRADE, _ep.getExchange(Exchanges.BTCTRADE).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Btctrade_cp = _cp;
				String bscp = Config.getInstance().get(Constants.BTCTrade_currency_pairs);
				if (bscp != null) {
					Btctrade_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Btctrade_cp.add(new CurrencyPair(pair));
					}
				} else {
					Btctrade_cp = _ep.getExchange(Exchanges.BTCTRADE).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Btctrade_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.BTCTrade_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getBtctradeFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Btctrade
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.BTCTrade_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getBtctradeFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Btctrade
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.BTCTrade_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getBtctradeFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}