/*
 * Copyright (C) 2012~2015 dinstone<dinstone@163.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dinstone.beanstalkc;

/**
 * The client/server sent a command that the server/client does not know.
 * 
 * @author guojinfei
 * @version 1.0.0
 */
public class UnknownCommandException extends RuntimeException {

    /**  */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public UnknownCommandException() {
    }

    /**
     * @param message
     */
    public UnknownCommandException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public UnknownCommandException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public UnknownCommandException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public UnknownCommandException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}