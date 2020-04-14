package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.enigma.EnigmaExchange;

public class Enigma extends GenericExchange {
	public Enigma() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(EnigmaExchange.class.getName());
	}
}