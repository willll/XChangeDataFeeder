package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.lakebtc.LakeBTCExchange;

public class Lakebtc extends GenericExchange {
	public Lakebtc() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(LakeBTCExchange.class.getName());
	}
}