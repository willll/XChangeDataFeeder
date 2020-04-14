package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.koinim.KoinimExchange;

public class Koinim extends GenericExchange {
	public Koinim() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(KoinimExchange.class.getName());
	}
}