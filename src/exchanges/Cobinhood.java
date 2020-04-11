package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.cobinhood.CobinhoodExchange;

public class Cobinhood extends GenericExchange {
	public Cobinhood() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(CobinhoodExchange.class.getName());
	}
}