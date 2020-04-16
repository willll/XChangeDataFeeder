package exchanges;

import java.io.IOException;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.coinmarketcap.pro.v1.CmcExchange;

import utils.Config;
import utils.Constants;

public class Coinmarketcap extends GenericExchange {
	public Coinmarketcap() {
		try {
			this.exchange = ExchangeFactory.INSTANCE.createExchange(CmcExchange.class.getName(),
					Config.getInstance().get(Constants.coinmarketcap_API),
					Config.getInstance().get(Constants.coinmarketcap_secret));
		} catch (IOException e) {
			logger.error("Could not access to : " + e.getMessage() + " Cannot read property file");
			this.exchange = ExchangeFactory.INSTANCE.createExchange(CmcExchange.class.getName());
		}
	}
}