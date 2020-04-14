package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.cryptonit2.CryptonitExchange;

public class Cryptonit extends GenericExchange {
	public Cryptonit() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(CryptonitExchange.class.getName());
	}
}