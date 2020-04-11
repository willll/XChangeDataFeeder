package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.coinone.CoinoneExchange;

public class Coinone extends GenericExchange {
	public Coinone() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(CoinoneExchange.class.getName());
	}
}