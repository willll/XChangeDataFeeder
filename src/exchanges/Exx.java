package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.exx.EXXExchange;

public class Exx extends GenericExchange {
	public Exx() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(EXXExchange.class.getName());
	}
}