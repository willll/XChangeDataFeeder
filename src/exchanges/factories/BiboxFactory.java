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

public class BiboxFactory extends GenericFactory {

	BiboxFactory() {
		exchange = Exchanges.BIBOX;
		ticker_pub = "BIBOX_TICKER_PUB";
		orderbook_pub = "BIBOX_ORDERBOOK_PUB";
	}

	public static void bibox(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bibox_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.BIBOX, _ep.getExchange(Exchanges.BIBOX).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Bibox_cp = _cp;
				String bscp = Config.getInstance().get(Constants.bibox_currency_pairs);
				if (bscp != null) {
					Bibox_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Bibox_cp.add(new CurrencyPair(pair));
					}
				} else {
					Bibox_cp = _ep.getExchange(Exchanges.BIBOX).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Bibox_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.bibox_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getBiboxFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Bibox
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bibox_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getBiboxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Bibox
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.bibox_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getBiboxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}