package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.quoine.QuoineExchange;

public class Quoine extends GenericExchange {
	public Quoine() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(QuoineExchange.class.getName());
	}
}