package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.latoken.LatokenExchange;

public class Latoken extends GenericExchange {
	public Latoken() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(LatokenExchange.class.getName());
	}
}