package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bx.BxExchange;

public class Bx extends GenericExchange {
	public Bx() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(BxExchange.class.getName());
	}
}