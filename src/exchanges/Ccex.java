package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ccex.CCEXExchange;

public class Ccex extends GenericExchange {
	public Ccex() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(CCEXExchange.class.getName());
	}
}