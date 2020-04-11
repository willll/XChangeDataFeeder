package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bl3p.Bl3pExchange;

public class Bl3p extends GenericExchange {
	public Bl3p() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(Bl3pExchange.class.getName());
	}
}