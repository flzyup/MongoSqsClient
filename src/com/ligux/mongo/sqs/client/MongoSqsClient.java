package com.ligux.mongo.sqs.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ligux.mongo.sqs.client.util.Util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Version 1.0
 * <p/>
 * Date: 2011-07-23 21:03
 * Author: flzyup@ligux.com
 * <p/>
 * Copyright © 2009-2014 LiGux.com.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * MongoSqsClient use to connect the MongoSqsServer
 */
public class MongoSqsClient {

    protected MongoSqsClient() {}

    private String httpPost(String url, String postData) throws MongoSqsClientException {
        return this.getSource(url, postData);
    }

    private String httpGet(String url) throws MongoSqsClientException {
        return this.getSource(url, null);
    }

    private String getSource(String urlStr, String postData) throws MongoSqsClientException {
        HttpURLConnection connection = null;
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader reader = null;
        OutputStream os = null;
        OutputStreamWriter osw = null;
        StringBuffer sb = new StringBuffer();

        try {
            URL url = new URL(MongoSqsClient4j.prefix + urlStr);
            connection = (HttpURLConnection)url.openConnection();
            if (postData != null) {
                try {
                    connection.setDoOutput(true);
                    os = connection.getOutputStream();
                    osw = new OutputStreamWriter(os, MongoSqsClient4j.charset);
                    osw.write(postData);
                    osw.flush();
                } catch (IOException e) {
                    throw new MongoSqsClientException("Send data error.", (Throwable) e);
                } finally {
                    if (osw != null) {
                        osw.close();
                    }
                    if (os != null) {
                        os.close();
                    }
                }
            }
            is = connection.getInputStream();
            isr = new InputStreamReader(is, MongoSqsClient4j.charset);
            reader = new BufferedReader(isr);
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n") ;
            }

        } catch (IOException e) {
            throw new MongoSqsClientException("Cannot connect to server.", (Throwable) e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        String sbs = sb.toString();
        System.out.println(sbs);
        if (sbs.contains("exception")) {
            throw new MongoSqsClientException("Global error.");
        }

        return sbs;
    }

    public MongoSqsStatus getStatus(String queueName) throws MongoSqsClientException {
        String urlStr = "opt=status_json&name=" + queueName;

        MongoSqsStatus status = null;

        String src = this.httpGet(urlStr);
        JSONObject returnStatus = JSON.parseObject(src);
        if (returnStatus != null) {
            status = new MongoSqsStatus();
            status.setMongoVersion(String.valueOf(returnStatus.get("mongoVersion")));
            status.setName(String.valueOf(returnStatus.get("name")));
            status.setUnreadNumber(Long.valueOf(String.valueOf(returnStatus.get("unreadNumber"))));
            status.setVersion(String.valueOf(returnStatus.get("version")));
        }
        return status;
    }

    public String put(String queueName, Object object) throws MongoSqsClientException, UnsupportedEncodingException {
        String urlStr = "opt=push&name=" + queueName;
        try {
            String data = Util.isJsonParsable(object) ? URLEncoder.encode(JSON.toJSONString(object), "utf-8") + "&json=1" : (String)object + "&json=0";

            String src = this.httpPost(urlStr, "data=" + data);
            if (src.contains("exception")) {
                throw new MongoSqsClientException("Push to queue: " + queueName + " got an exception.");
            }
            return src;
        } catch (MongoSqsClientException e) {
            throw e;
        } catch (UnsupportedEncodingException e) {
            throw e;
        }
    }

    public String getString(String queueName) throws MongoSqsClientException {
        String urlStr = "opt=pop&name=" + queueName;
        String src = this.httpGet(urlStr);
        if (src.contains("exception")) {
                throw new MongoSqsClientException("Pop from queue: " + queueName + " got an exception.");
        }
        return src;
    }

    public Object getObject(String queueName, Class c) throws MongoSqsClientException {
        String src = getString(queueName);
        return JSON.parseObject(src, c);
    }

    public boolean reset(String queueName) throws MongoSqsClientException {
        String urlStr = "opt=clear&name=" + queueName;
        String src = this.httpGet(urlStr);
        return src.contains("STATUS: OK");
    }

    public static void main(String[] args) {
        try {
            MongoSqsClient4j.setConnectionInfo("xxx.xxx.xxx.xxx", 80, "utf-8");
        } catch (MongoSqsClientException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        MongoSqsClient client = MongoSqsClient4j.createNewClient();
        String s = null;
        try {
            s = client.getString("test_queue");
        } catch (MongoSqsClientException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        System.out.println(s);
;    }
}
