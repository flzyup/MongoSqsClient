package com.ligux.mongo.sqs.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Version 1.0
 * <p/>
 * Date: 2014-08-23 21:03
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
 * Client helper
 */
public class MongoSqsClient4j {
    protected static String prefix;

    protected static String charset;

    protected static boolean configured = false;

    public static void setConnectionInfo(String ip, int port, String charset) throws MongoSqsClientException {
        try {
            "".getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new MongoSqsClientException("Unknown charset.", (Throwable) e);
        }
        URL url;
        HttpURLConnection connection = null;
        String prefix = "http://" + ip + ":" + port + "/mongosqs";

        try {
            url = new URL(prefix);
            connection = (HttpURLConnection)url.openConnection();
            connection.connect();
        } catch (IOException e) {
            throw new MongoSqsClientException(prefix + " cannot located.", (Throwable) e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        MongoSqsClient4j.prefix = prefix + "?";
        MongoSqsClient4j.charset = charset;
        MongoSqsClient4j.configured = true;
    }

    public static MongoSqsClient createNewClient() {
        return new MongoSqsClient();
    }
}
