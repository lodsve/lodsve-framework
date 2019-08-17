/*
 * Copyright (C) 2019  Sun.Hao
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package lodsve.rocketmq.configuration;

import lodsve.rocketmq.annotations.RocketMQTransactionListener;
import lodsve.rocketmq.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RocketMQ Transaction Annotation Processor.
 *
 * @author 孙昊(Hulk)
 */
public class RocketMQTransactionAnnotationProcessor
        implements BeanPostProcessor, Ordered, BeanFactoryAware {
    private final static Logger log = LoggerFactory.getLogger(RocketMQTransactionAnnotationProcessor.class);
    private final Set<Class<?>> nonProcessedClasses = Collections.newSetFromMap(new ConcurrentHashMap<Class<?>, Boolean>(64));
    private BeanFactory beanFactory;
    private TransactionHandlerRegistry transactionHandlerRegistry;

    public RocketMQTransactionAnnotationProcessor(TransactionHandlerRegistry transactionHandlerRegistry) {
        this.transactionHandlerRegistry = transactionHandlerRegistry;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!this.nonProcessedClasses.contains(bean.getClass())) {
            Class<?> targetClass = AopUtils.getTargetClass(bean);
            RocketMQTransactionListener listener = AnnotationUtils.findAnnotation(targetClass, RocketMQTransactionListener.class);
            this.nonProcessedClasses.add(bean.getClass());
            // for quick search
            if (listener == null) {
                log.trace("No @RocketMQTransactionListener annotations found on bean type: {}", bean.getClass());
            } else {
                try {
                    processTransactionListenerAnnotation(listener, bean);
                } catch (MQClientException e) {
                    log.error("Failed to process annotation " + listener, e);
                    throw new BeanCreationException("Failed to process annotation " + listener, e);
                }
            }
        }

        return bean;
    }

    private void processTransactionListenerAnnotation(RocketMQTransactionListener listener, Object bean)
            throws MQClientException {
        if (transactionHandlerRegistry == null) {
            throw new MQClientException("Bad usage of @RocketMQTransactionListener, the class must work with RocketMQTemplate", null);
        }
        if (!RocketMQLocalTransactionListener.class.isAssignableFrom(bean.getClass())) {
            throw new MQClientException("Bad usage of @RocketMQTransactionListener, the class must implement interface RocketMQLocalTransactionListener", null);
        }
        TransactionHandler transactionHandler = new TransactionHandler();
        transactionHandler.setBeanFactory(this.beanFactory);
        transactionHandler.setName(listener.txProducerGroup());
        transactionHandler.setBeanName(bean.getClass().getName());
        transactionHandler.setListener((RocketMQLocalTransactionListener) bean);
        transactionHandler.setCheckExecutor(listener.corePoolSize(), listener.maximumPoolSize(), listener.keepAliveTime(), listener.blockingQueueSize());

        transactionHandlerRegistry.registerTransactionHandler(transactionHandler);
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

}
