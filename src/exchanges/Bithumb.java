package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bithumb.BithumbExchange;

public class Bithumb extends GenericExchange {
	public Bithumb() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(BithumbExchange.class.getName());
	}
}