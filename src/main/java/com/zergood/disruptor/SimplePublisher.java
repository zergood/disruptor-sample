package com.zergood.disruptor;

import com.lmax.disruptor.RingBuffer;

public class SimplePublisher {
    private RingBuffer<long[]> ringBuffer;

    public SimplePublisher(RingBuffer<long[]> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void publish(){
        long next = ringBuffer.next();

        long[] longs = ringBuffer.get(next);
        longs[0] = 1;

        ringBuffer.publish(next);
    }
}
