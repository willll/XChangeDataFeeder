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

public class TradeogreFactory extends GenericFactory {

	TradeogreFactory() {
		exchange = Exchanges.TRADEOGRE;
		ticker_pub = "TRADEOGRE_TICKER_PUB";
		orderbook_pub = "TRADEOGRE_ORDERBOOK_PUB";
	}

	public static void TradeOgre(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.TradeOgre_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.TRADEOGRE, _ep.getExchange(Exchanges.TRADEOGRE).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Tradeogre_cp = _cp;
				String bscp = Config.getInstance().get(Constants.TradeOgre_currency_pairs);
				if (bscp != null) {
					Tradeogre_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Tradeogre_cp.add(new CurrencyPair(pair));
					}
				} else {
					Tradeogre_cp = _ep.getExchange(Exchanges.TRADEOGRE).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Tradeogre_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.TradeOgre_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getTradeogreFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Tradeogre
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.TradeOgre_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getTradeogreFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Tradeogre
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.TradeOgre_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getTradeogreFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}