package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.luno.LunoExchange;

public class Luno extends GenericExchange {
	public Luno() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(LunoExchange.class.getName());
	}
}