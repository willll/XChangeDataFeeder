package tasks;

import java.util.Set;

import org.knowm.xchange.currency.CurrencyPair;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import exchanges.IExchange;
import features.ILogger;

/**
 * @author will
 *
 * @param <T>
 */
public class PublisherTask<T> implements ITask, ILogger {

	/**
	 * @param id
	 * @param exchange
	 * @param currencyPairs
	 * @param context
	 */
	public PublisherTask(final String id, final T exchange, Set<CurrencyPair> currencyPairs,
			final ZContext context, final long refreshRate) {
		super();
		this.id = id;
		this.exchange = exchange;
		this.currencyPairs = currencyPairs;
		this.mapper = new ObjectMapper();
		this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
		this.threadId = Thread.currentThread().getId();
		this.exchangeName = ((IExchange) exchange).toString();

		this.socket = context.createSocket(SocketType.PUSH);
		this.socket.connect("inproc://workers");
		this.exit = false;
		this.refreshRate = refreshRate;
	}

	public void stop() {
		exit = true;
	}

	protected String id;
	protected T exchange;
	protected Set<CurrencyPair> currencyPairs;
	protected ObjectMapper mapper;
	protected long threadId;
	protected String exchangeName;
	protected ZMQ.Socket socket;
	protected boolean exit;
	protected long refreshRate;

	@Override

	public void run() {
		assert false;

	}
}
