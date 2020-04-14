package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.btcc.BTCCExchange;

public class Btcc extends GenericExchange {
	public Btcc() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(BTCCExchange.class.getName());
	}
}