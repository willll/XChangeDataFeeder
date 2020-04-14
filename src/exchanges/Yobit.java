package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.yobit.YoBitExchange;

public class Yobit extends GenericExchange {
	public Yobit() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(YoBitExchange.class.getName());
	}
}