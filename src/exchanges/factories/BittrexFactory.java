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

public class BittrexFactory extends GenericFactory {

	BittrexFactory() {
		exchange = Exchanges.BITTREX;
		ticker_pub = "BITTREX_TICKER_PUB";
		orderbook_pub = "BITTREX_ORDERBOOK_PUB";
	}

	public static void bittrex(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bittrex_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.BITTREX, _ep.getExchange(Exchanges.BITTREX).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Bittrex_cp = _cp;
				String bscp = Config.getInstance().get(Constants.bittrex_currency_pairs);
				if (bscp != null) {
					Bittrex_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Bittrex_cp.add(new CurrencyPair(pair));
					}
				} else {
					Bittrex_cp = _ep.getExchange(Exchanges.BITTREX).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Bittrex_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.bittrex_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getAcxFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Bittrex
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bittrex_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Bittrex
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bittrex_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}