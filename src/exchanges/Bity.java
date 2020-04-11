package exchanges;

import java.io.IOException;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bity.BityExchange;
import org.knowm.xchange.coinmarketcap.pro.v1.CmcExchange;

import utils.Config;
import utils.Constants;

public class Bity extends GenericExchange {
	public Bity() {
		try {
			this.exchange = ExchangeFactory.INSTANCE.createExchange(BityExchange.class.getName(),
					Config.getInstance().get(Constants.bity_API), Config.getInstance().get(Constants.bity_secret));
		} catch (IOException e) {
			logger.error("Could not access to : " + e.getMessage() + " Cannot read property file");
			this.exchange = ExchangeFactory.INSTANCE.createExchange(BityExchange.class.getName());
		}
	}
}