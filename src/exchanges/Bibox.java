package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bibox.BiboxExchange;

public class Bibox extends GenericExchange {
	public Bibox() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(BiboxExchange.class.getName());
	}
}