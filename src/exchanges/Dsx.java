package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.dsx.DSXExchange;

public class Dsx extends GenericExchange {
	public Dsx() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(DSXExchange.class.getName());
	}
}