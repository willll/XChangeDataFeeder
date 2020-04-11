package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.coinfloor.CoinfloorExchange;

public class Coinfloor extends GenericExchange {
	public Coinfloor() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(CoinfloorExchange.class.getName());
	}
}