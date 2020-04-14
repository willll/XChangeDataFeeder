package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.huobi.HuobiExchange;

public class Huobi extends GenericExchange {
	public Huobi() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(HuobiExchange.class.getName());
	}
}