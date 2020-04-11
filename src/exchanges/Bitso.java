package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bitso.BitsoExchange;

public class Bitso extends GenericExchange {
	public Bitso() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(BitsoExchange.class.getName());
	}
}