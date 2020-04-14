package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.globitex.GlobitexExchange;

public class Globitex extends GenericExchange {
	public Globitex() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(GlobitexExchange.class.getName());
	}
}