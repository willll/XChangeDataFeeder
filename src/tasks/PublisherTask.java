package tasks;

import java.util.Set;

import org.influxdb.BatchOptions;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.knowm.xchange.currency.CurrencyPair;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import exchanges.IExchange;
import features.ILogger;
import utils.Config;
import utils.Constants;

import java.io.IOException;

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
	public PublisherTask(final String id, final T exchange, Set<CurrencyPair> currencyPairs, final ZContext context,
			final long refreshRate) throws IOException {
		super();
		this.id = id;
		this.exchange = exchange;
		this.currencyPairs = currencyPairs;
		this.mapper = new ObjectMapper();
		this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
		this.threadId = Thread.currentThread().getId();

		this.exchangeName = ((IExchange) exchange).toString();

		// @TODO : Fix this bad design, this should be passed as a template parameter

		// ZeroMQ
		this.socket = context.createSocket(SocketType.PUSH);
		this.socket.connect("inproc://workers");

		// InfluxDB
		String rpName = "autogen";
		this.influxDB = InfluxDBFactory.connect(
				"http://" + Config.getInstance().get(Constants.influxdb_address) + ":"
						+ Config.getInstance().get(Constants.influxdb_port),
				Config.getInstance().get(Constants.influxdb_username),
				Config.getInstance().get(Constants.influxdb_password));
		this.influxDB.createDatabase(this.id);
		this.influxDB.setDatabase(this.id);
		this.influxDB.setRetentionPolicy(rpName);

		this.influxDB.enableBatch(BatchOptions.DEFAULTS);

		String tmp = Config.getInstance().get(Constants.influxdb_enable);
		
		this.influxDBEnable = Config.isEnabled(Constants.influxdb_enable);
		this.zeroMQEnable = Config.isEnabled(Constants.zeromq_enable);

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
	protected boolean zeroMQEnable;
	protected ZMQ.Socket socket;
	protected boolean influxDBEnable;
	protected InfluxDB influxDB;
	protected boolean exit;
	protected long refreshRate;

	public boolean isInfluxDBEnable() {
		return influxDBEnable;
	}

	public boolean isZeroMQEnable() {
		return zeroMQEnable;
	}

	@Override

	public void run() {
		assert false;

	}
}
