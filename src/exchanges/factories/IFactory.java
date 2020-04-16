package exchanges.factories;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import org.knowm.xchange.currency.CurrencyPair;
import org.zeromq.ZContext;

public interface IFactory {

	ArrayList<Thread> create_orderbook_feeders(EntryPoint ep, ZContext context, Set<CurrencyPair> cp)
			throws IOException;

	ArrayList<Thread> create_ticker_feeders(EntryPoint ep, ZContext context, Set<CurrencyPair> cp)
			throws IOException;

}
