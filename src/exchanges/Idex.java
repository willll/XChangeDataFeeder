package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.idex.IdexExchange;

public class Idex extends GenericExchange {
	public Idex() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(IdexExchange.class.getName());
	}
}