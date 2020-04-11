package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.itbit.ItBitExchange;

public class Itbit extends GenericExchange {
	public Itbit() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(ItBitExchange.class.getName());
	}
}