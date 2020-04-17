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

public class BtcmarketsFactory extends GenericFactory {

	BtcmarketsFactory() {
		exchange = Exchanges.BTCMARKETS;
		ticker_pub = "BTCMARKETS_TICKER_PUB";
		orderbook_pub = "BTCMARKETS_ORDERBOOK_PUB";
	}

	public static void BTCMarkets(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.BTCMarkets_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.BTCMARKETS, _ep.getExchange(Exchanges.BTCMARKETS).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Btcmarkets_cp = _cp;
				String bscp = Config.getInstance().get(Constants.BTCMarkets_currency_pairs);
				if (bscp != null) {
					Btcmarkets_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Btcmarkets_cp.add(new CurrencyPair(pair));
					}
				} else {
					Btcmarkets_cp = _ep.getExchange(Exchanges.BTCMARKETS).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Btcmarkets_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.BTCMarkets_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getBtcmarketsFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Btcmarkets
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.BTCMarkets_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getBtcmarketsFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Btcmarkets
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.BTCMarkets_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getBtcmarketsFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}