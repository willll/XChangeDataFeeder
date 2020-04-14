package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.deribit.v2.DeribitExchange;

public class Deribit extends GenericExchange {
	public Deribit() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(DeribitExchange.class.getName());
	}
}