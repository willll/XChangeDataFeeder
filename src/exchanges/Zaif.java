package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.zaif.ZaifExchange;

public class Zaif extends GenericExchange {
	public Zaif() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(ZaifExchange.class.getName());
	}
}