package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.kucoin.KucoinExchange;

public class Kucoin extends GenericExchange {
	public Kucoin() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(KucoinExchange.class.getName());
	}
}