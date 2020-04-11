package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.vircurex.VircurexExchange;

public class Vircurex extends GenericExchange {
	public Vircurex() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(VircurexExchange.class.getName());
	}
}