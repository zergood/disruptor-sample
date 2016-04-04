package com.zergood.disruptor;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.YieldingWaitStrategy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {
    private static final int RINGBUFFERS_COUNT = 3;
    private static final EventFactory<long[]> FACTORY = new EventFactory<long[]>()
    {
        @Override
        public long[] newInstance()
        {
            return new long[1];
        }
    };
    private static final ExecutorService executor = Executors.newCachedThreadPool();
    private static final RingBuffer<long[]>[] buffers = new RingBuffer[RINGBUFFERS_COUNT];
    private static final SequenceBarrier[] barriers = new SequenceBarrier[RINGBUFFERS_COUNT];


    public static void main(String[] args) throws InterruptedException {
        SimpleHandler simpleHandler = new SimpleHandler();

        for (int i = 0; i < RINGBUFFERS_COUNT; i++) {
            buffers[i] = RingBuffer.createSingleProducer(FACTORY, 32, new YieldingWaitStrategy());
            barriers[i] = buffers[i].newBarrier();
        }

        SimplePublisher simplePublisher = new SimplePublisher(buffers[0]);

        MultiBufferBatchEventProcessor<long[]> batchProcessor = new MultiBufferBatchEventProcessor<>(buffers, barriers, simpleHandler);


        for (int i = 0; i < RINGBUFFERS_COUNT; i++)
        {
            buffers[i].addGatingSequences(batchProcessor.getSequences()[i]);
        }

        executor.submit(batchProcessor);

        simplePublisher.publish();
    }
}
