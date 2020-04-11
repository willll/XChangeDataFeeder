package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bitcoinium.BitcoiniumExchange;

public class Bitcoinium extends GenericExchange {
	public Bitcoinium() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(BitcoiniumExchange.class.getName());
	}
}