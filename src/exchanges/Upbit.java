package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.upbit.UpbitExchange;

public class Upbit extends GenericExchange {
	public Upbit() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(UpbitExchange.class.getName());
	}
}