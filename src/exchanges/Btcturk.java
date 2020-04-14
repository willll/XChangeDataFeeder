package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.btcturk.BTCTurkExchange;

public class Btcturk extends GenericExchange {
	public Btcturk() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(BTCTurkExchange.class.getName());
	}
}