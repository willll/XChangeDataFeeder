package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bleutrade.BleutradeExchange;

public class Bleutrade extends GenericExchange {
	public Bleutrade() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(BleutradeExchange.class.getName());
	}
}