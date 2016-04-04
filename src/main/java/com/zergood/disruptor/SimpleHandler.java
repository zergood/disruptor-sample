package com.zergood.disruptor;

import com.lmax.disruptor.EventHandler;

public class SimpleHandler implements EventHandler<long[]> {
    @Override
    public void onEvent(long[] event, long sequence, boolean endOfBatch) throws Exception {
        if(!endOfBatch) throw new Exception("batch detected");
    }
}
