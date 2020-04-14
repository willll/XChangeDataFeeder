package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.coinex.CoinexExchange;

public class Coinex extends GenericExchange {
	public Coinex() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(CoinexExchange.class.getName());
	}
}