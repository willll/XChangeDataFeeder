package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bitbay.v3.BitbayExchange;

public class Bitbay extends GenericExchange {
	public Bitbay() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(BitbayExchange.class.getName());
	}
}
