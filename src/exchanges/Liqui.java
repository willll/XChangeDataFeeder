package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.liqui.LiquiExchange;

public class Liqui extends GenericExchange {
	public Liqui() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(LiquiExchange.class.getName());
	}
}