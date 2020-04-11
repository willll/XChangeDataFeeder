package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bitz.BitZExchange;;

public class Bitz extends GenericExchange {
	public Bitz() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(BitZExchange.class.getName());
	}
}