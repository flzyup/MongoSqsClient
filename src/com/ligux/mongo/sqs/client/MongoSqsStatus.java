package com.ligux.mongo.sqs.client;

import com.alibaba.fastjson.JSON;

/**
 * Package: com.daguu.mongo.sqs.server.model
 * <p/>
 * Created by IntelliJ IDEA.
 * User: Johnny Lu
 * Date: 7/19/11
 * Time: 4:58 PM
 */
public class MongoSqsStatus {

    private String name;

//    private long maxNumber;

    private long unreadNumber;

    private String version;

    private String mongoVersion;

    public MongoSqsStatus() {

    }

    public MongoSqsStatus(String name, long unreadNumber, String version, String mongoVersion) {
        this.name = name;
//        this.maxNumber = maxNumber;
        this.unreadNumber = unreadNumber;
        this.version = version;
        this.mongoVersion = mongoVersion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public long getMaxNumber() {
//        return maxNumber;
//    }
//
//    public void setMaxNumber(long maxNumber) {
//        this.maxNumber = maxNumber;
//    }

    public long getUnreadNumber() {
        return unreadNumber;
    }

    public void setUnreadNumber(long unreadNumber) {
        this.unreadNumber = unreadNumber;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMongoVersion() {
        return mongoVersion;
    }

    public void setMongoVersion(String mongoVersion) {
        this.mongoVersion = mongoVersion;
    }

    @Override
    public String toString() {
        return JSON.toJSON(this).toString();
    }

    public String toString(boolean toHumanReadable) {
        if (!toHumanReadable) {
            return this.toString();
        }
        return this.version + "\n" +
                "Mongo version: " + this.mongoVersion + "\n" +
                "\n" +
                "Quene name: " + this.name + "\n" +
//                "Maxium number of queues: " + this.maxNumber + "\n" +
                "Unread number of this queue: " + this.unreadNumber;
    }
}
