package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.gateio.GateioExchange;

public class Gateio extends GenericExchange {
	public Gateio() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(GateioExchange.class.getName());
	}
}