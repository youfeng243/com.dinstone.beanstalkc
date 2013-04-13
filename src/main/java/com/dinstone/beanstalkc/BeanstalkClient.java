/*
 * Copyright (C) 2012~2013 dinstone<dinstone@163.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.dinstone.beanstalkc;

import java.util.concurrent.TimeUnit;

import com.dinstone.beanstalkc.internal.Connection;
import com.dinstone.beanstalkc.internal.ConnectionFactory;
import com.dinstone.beanstalkc.internal.OperationFuture;
import com.dinstone.beanstalkc.internal.operation.BuryOperation;
import com.dinstone.beanstalkc.internal.operation.DeleteOperation;
import com.dinstone.beanstalkc.internal.operation.IgnoreOperation;
import com.dinstone.beanstalkc.internal.operation.PutOperation;
import com.dinstone.beanstalkc.internal.operation.QuitOperation;
import com.dinstone.beanstalkc.internal.operation.ReleaseOperation;
import com.dinstone.beanstalkc.internal.operation.ReserveOperation;
import com.dinstone.beanstalkc.internal.operation.TouchOperation;
import com.dinstone.beanstalkc.internal.operation.UseOperation;
import com.dinstone.beanstalkc.internal.operation.WatchOperation;

/**
 * This is the client implementation of the beanstalkd protocol.
 * 
 * @author guojf
 * @version 1.0.0.2013-4-11
 */
public class BeanstalkClient implements JobProducer, JobConsumer {

    private Connection connection;

    private long optionTimeout;

    private Configuration config;

    public BeanstalkClient() {
        this(new Configuration());
    }

    /**
     * @param config
     */
    public BeanstalkClient(Configuration config) {
        if (config == null) {
            throw new IllegalArgumentException("config is null");
        }
        this.config = config;
        this.optionTimeout = config.getLong(Configuration.OPTION_TIMEOUT, 1);

        ConnectionFactory factory = ConnectionFactory.getInstance();
        this.connection = factory.createConnection(config);
    }

    @Override
    public boolean useTube(String tube) {
        UseOperation operation = new UseOperation(tube);
        return getBoolean(connection.handle(operation));
    }

    @Override
    public boolean watchTube(String tube) {
        WatchOperation operation = new WatchOperation(tube);
        return getBoolean(connection.handle(operation));
    }

    @Override
    public boolean ignoreTube(String tube) {
        IgnoreOperation operation = new IgnoreOperation(tube);
        return getBoolean(connection.handle(operation));
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.dinstone.beanstalkc.JobProducer#putJob(int, int, int, byte[])
     */
    @Override
    public long putJob(int priority, int delay, int ttr, byte[] data) {
        PutOperation operation = new PutOperation(priority, delay, ttr, data);
        OperationFuture<Long> future = connection.handle(operation);
        try {
            return future.get(optionTimeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteJob(long id) {
        DeleteOperation operation = new DeleteOperation(id);
        return getBoolean(connection.handle(operation));
    }

    @Override
    public boolean touchJob(long id) {
        TouchOperation operation = new TouchOperation(id);
        return getBoolean(connection.handle(operation));
    }

    @Override
    public Job reserveJob(long timeout) {
        ReserveOperation operation = new ReserveOperation(timeout);
        OperationFuture<Job> future = connection.handle(operation);
        try {
            return future.get(optionTimeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean releaseJob(long id, int priority, int delay) {
        ReleaseOperation operation = new ReleaseOperation(id, priority, delay);
        return getBoolean(connection.handle(operation));
    }

    @Override
    public boolean buryJob(long id, int priority) {
        BuryOperation operation = new BuryOperation(id, priority);
        return getBoolean(connection.handle(operation));
    }

    public void quit() {
        QuitOperation operation = new QuitOperation();
        getBoolean(connection.handle(operation));
    }

    @Override
    public void close() {
        connection.close();

        ConnectionFactory factory = ConnectionFactory.getInstance();
        factory.releaseConnection(config);
    }

    private boolean getBoolean(OperationFuture<Boolean> future) {
        try {
            return future.get(optionTimeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            return false;
        }
    }
}
