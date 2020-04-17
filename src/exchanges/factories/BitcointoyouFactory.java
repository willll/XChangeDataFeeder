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

public class BitcointoyouFactory extends GenericFactory {

	BitcointoyouFactory() {
		exchange = Exchanges.BITCOINTOYOU;
		ticker_pub = "BITCOINTOYOU_TICKER_PUB";
		orderbook_pub = "BITCOINTOYOU_ORDERBOOK_PUB";
	}

	public static void bitcointoyou(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitcointoyou_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.BITCOINTOYOU, _ep.getExchange(Exchanges.BITCOINTOYOU).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Bitcointoyou_cp = _cp;
				String bscp = Config.getInstance().get(Constants.bitcointoyou_currency_pairs);
				if (bscp != null) {
					Bitcointoyou_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Bitcointoyou_cp.add(new CurrencyPair(pair));
					}
				} else {
					Bitcointoyou_cp = _ep.getExchange(Exchanges.BITCOINTOYOU).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Bitcointoyou_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.bitcointoyou_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getBitcointoyouFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Bitcointoyou
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitcointoyou_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getBitcointoyouFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Bitcointoyou
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitcointoyou_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getBitcointoyouFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}