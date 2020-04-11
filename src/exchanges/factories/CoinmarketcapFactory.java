package exchanges.factories;

import java.io.IOException;

import exchanges.factories.EntryPoint.Exchanges;
import utils.Config;
import utils.Constants;

public class CoinmarketcapFactory extends GenericFactory {

	CoinmarketcapFactory() {
		exchange = Exchanges.COINMARKETCAP;
		ticker_pub = "COINMARKETCAP_TICKER_PUB";
		orderbook_pub = "COINMARKETCAP_ORDERBOOK_PUB";

		// it is supposed to be refreshed every 60 seconds or so
		// But free API grants only only ticker per 10 minutes
		try {
			refresh_rate = Long.parseLong(Config.getInstance().get(Constants.coinmarketcap_refresh_rate)) * 1000;
		} catch (NumberFormatException e) {
			logger.error("Could not convert : " + e.getMessage() + " fallback to default refresh rate : "
					+ Constants.COINMARKETCAP_DEFAULT_REFRESH_RATE);
			refresh_rate = Constants.COINMARKETCAP_DEFAULT_REFRESH_RATE;
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("Could not open property file : " + e.getMessage() + " fallback to default refresh rate : "
					+ Constants.COINMARKETCAP_DEFAULT_REFRESH_RATE);
			refresh_rate = Constants.COINMARKETCAP_DEFAULT_REFRESH_RATE;
			e.printStackTrace();
		}
	}
}
