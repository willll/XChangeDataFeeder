package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.cryptowatch.CryptowatchExchange;

public class Cryptowatch extends GenericExchange {
	public Cryptowatch() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(CryptowatchExchange.class.getName());
	}
}