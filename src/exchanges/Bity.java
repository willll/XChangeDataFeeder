package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bity.BityExchange;

public class Bity extends GenericExchange {
	public Bity() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(BityExchange.class.getName());
	}
}