package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bitmarket.BitMarketExchange;;

public class Bitmarket extends GenericExchange {
	public Bitmarket() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(BitMarketExchange.class.getName());
	}
}