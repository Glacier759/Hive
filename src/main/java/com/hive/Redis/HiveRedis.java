package com.hive.Redis;

import redis.clients.jedis.Jedis;

import java.util.HashSet;

/**
 * Created by Renlixiang on 14-5-7.
 */

public class HiveRedis {

    private String Key;
    private Jedis Redis;
    private HashSet<String> Keys = new HashSet<String>();
    public HiveRedis(){
        //mo ren chu shi hua
        this.ConnectRedis();
    }

    public void ConnectRedis(String host, int port) {
        Redis = new Jedis(host, port);
    }
    public void ConnectRedis( String host ) {
        Redis = new Jedis(host, 6379);
    }
    public void ConnectRedis() {
        Redis = new Jedis("222.24.63.100", 6379);
    }
    public void setKey( String Key ) {
        this.Key = Key;
        Keys.add(Key);
    }
    public long getKeySize() {
        return Keys.size();
    }
    public String getKey() {
        return Key;
    }
    public String getValue() {
        return Redis.lindex(Key, Redis.llen(Key) - 1);
    }
    public String popValue() {
        return Redis.rpop(Key);
    }
    public long pushValue( String Value ) {
        Redis.lpush(Key, Value);
        return Redis.llen(Key);
    }
    public long getLegnth( String Key ) {
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
}