package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.acx.AcxExchange;

public class Acx extends GenericExchange {
	public Acx() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(AcxExchange.class.getName());
	}
}