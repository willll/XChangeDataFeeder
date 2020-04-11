package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.anx.v2.ANXExchange;;

public class Anx extends GenericExchange {
	public Anx() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(ANXExchange.class.getName());
	}
}