package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bankera.BankeraExchange;

public class Bankera extends GenericExchange {
	public Bankera() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(BankeraExchange.class.getName());
	}
}