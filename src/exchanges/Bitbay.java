package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bitbay.BitbayExchange;

public class Bitbay extends GenericExchange {
	public Bitbay() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(BitbayExchange.class.getName());
	}
}