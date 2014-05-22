
import java.util.HashSet;

import redis.clients.jedis.Jedis;

public class HiveRedis implements Runnable {
	static private String SecUsername = null;
	private String Key;
	private Jedis Redis;
	private HashSet<String> Keys = new HashSet<String>();
	private long ChangeCount, LastLength, NowLength = 0;

/*	public static void main(String[] args) {
	//	HiveRedis obj = new HiveRedis();
		//obj.ConnectRedis("222.24.63.100");
		//obj.setKey("URL");
		//System.out.println(obj.getLength());
		//obj.clearRedis();
		//System.out.println(obj.getLength());
	}
*/
	public long Sechange() {
		if ( SecUsername == null ) {
			return 0;
		}
		else {
			return this.ChangeCount;
		}
	}
	
	public void ConnectRedis(String host, int port) {
		Redis = new Jedis(host, port);
		new Thread(this).start();
	}
	public void ConnectRedis( String host ) {
		Redis = new Jedis(host, 6379);
		new Thread(this).start();
	}
	public void ConnectRedis() {
		Redis = new Jedis("127.0.0.1", 6379);
		new Thread(this).start();
	}
	public void setKey( String Key ) {
		this.Key = Key;
		Keys.add(Key);
		SecUsername = "user";
	}
	public long getKeySize() {
		return Keys.size();
	}
	public String getKey() {
		return Key;
	}
	public String getValue() {
		return Redis.lindex(Key, Redis.llen(Key)-1);
	}
	public String popValue() {
		return Redis.rpop(Key);
	}
	public long pushValue( String Value ) {
		Redis.lpush(Key, Value);
		return Redis.llen(Key);
	}
	
	public long getLength( String Key ) { 	//返回URL的个数
		return Redis.llen(Key);
	}
	public long getLength() {
		return Redis.llen(Key);
	}
	public String indexValue( long index ) {
		return Redis.lindex(Key, Redis.llen(Key)-1-index);
	}
	public void delKey() {
		Redis.del(Key);
	}
	public String clearRedis() {
		return Redis.flushDB();
	}

	public void run() {
		try {
			while ( true ) {
				LastLength = NowLength;
				Thread.sleep(1000);
				NowLength = this.getLength();
				ChangeCount = NowLength-LastLength;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
