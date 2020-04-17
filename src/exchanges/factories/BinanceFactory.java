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

public class BinanceFactory extends GenericStreamingFactory {

	BinanceFactory() {
		exchange = Exchanges.BINANCE;
		ticker_pub = "BINANCE_TICKER_PUB";
		orderbook_pub = "BINANCE_ORDERBOOK_PUB";
	}

	public static void binance(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.binance_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.BINANCE, _ep.getExchange(Exchanges.BINANCE).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Binance_cp = _cp;
				String bscp = Config.getInstance().get(Constants.binance_currency_pairs);
				if (bscp != null) {
					Binance_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Binance_cp.add(new CurrencyPair(pair));
					}
				} else {
					Binance_cp = _ep.getExchange(Exchanges.BINANCE).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Binance_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Create a ticker from Binance
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.binance_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getBinanceFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Binance
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.binance_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getBinanceFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}