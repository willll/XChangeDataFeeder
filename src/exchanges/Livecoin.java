package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.livecoin.LivecoinExchange;

public class Livecoin extends GenericExchange {
	public Livecoin() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(LivecoinExchange.class.getName());
	}
}