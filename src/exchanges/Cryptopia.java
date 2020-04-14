package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.cryptopia.CryptopiaExchange;

public class Cryptopia extends GenericExchange {
	public Cryptopia() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(CryptopiaExchange.class.getName());
	}
}