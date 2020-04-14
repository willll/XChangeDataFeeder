package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.tradeogre.TradeOgreExchange;

public class Tradeogre extends GenericExchange {
	public Tradeogre() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(TradeOgreExchange.class.getName());
	}
}