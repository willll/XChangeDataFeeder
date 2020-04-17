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

public class MercadobitcoinFactory extends GenericFactory {

	MercadobitcoinFactory() {
		exchange = Exchanges.MERCADOBITCOIN;
		ticker_pub = "MERCADOBITCOIN_TICKER_PUB";
		orderbook_pub = "MERCADOBITCOIN_ORDERBOOK_PUB";
	}

	public static void MercadoBitcoin(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx) throws IOException {
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.MercadoBitcoin_enabled))) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(Exchanges.MERCADOBITCOIN, _ep.getExchange(Exchanges.MERCADOBITCOIN).getCurrencyPairs());
			} else {
				Set<CurrencyPair> Mercadobitcoin_cp = _cp;
				String bscp = Config.getInstance().get(Constants.MercadoBitcoin_currency_pairs);
				if (bscp != null) {
					Mercadobitcoin_cp = new HashSet<>();
					for (String pair : bscp.split(",")) {
						Mercadobitcoin_cp.add(new CurrencyPair(pair));
					}
				} else {
					Mercadobitcoin_cp = _ep.getExchange(Exchanges.MERCADOBITCOIN).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!Mercadobitcoin_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(Constants.MercadoBitcoin_refresh_rate);
                if (refresh_timer != null) {
					ExchangesFactory.getAcxFactory().setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker from Mercadobitcoin
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.MercadoBitcoin_ticker_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(_ep, _ctx, _cp));
				}

				// Create an orderbook from Mercadobitcoin
				if (Boolean.parseBoolean(Config.getInstance().get(Constants.MercadoBitcoin_orderbook_enabled))) {
					_thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(_ep, _ctx, _cp));
				}
			}
		}
	}
}