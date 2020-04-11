package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ripple.RippleExchange;

public class Ripple extends GenericExchange {
	public Ripple() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(RippleExchange.class.getName());
	}
}