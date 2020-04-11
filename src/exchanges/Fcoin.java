package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.fcoin.FCoinExchange;

public class Fcoin extends GenericExchange {
	public Fcoin() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(FCoinExchange.class.getName());
	}
}