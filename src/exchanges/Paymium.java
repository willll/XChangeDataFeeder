package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.paymium.PaymiumExchange;

public class Paymium extends GenericExchange {
	public Paymium() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(PaymiumExchange.class.getName());
	}
}