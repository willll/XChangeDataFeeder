package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.mercadobitcoin.MercadoBitcoinExchange;;

public class Mercadobitcoin extends GenericExchange {
	public Mercadobitcoin() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(MercadoBitcoinExchange.class.getName());
	}
}