package processor.memorysystem;

import configuration.Configuration;
import generic.Element;
import generic.Event;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.MemoryWriteEvent;
import generic.Simulator;
import generic.Event.EventType;
import processor.Clock;
import processor.Processor;
import processor.memorysystem.Cacheline;
import processor.pipeline.MemoryAccess;

public class Cache implements Element {
    Processor containingProcessor;
    int cache_size;
    int lines_count;
    int cache_latency;
    int word_size;
    int count_words_in_line;
    int associativity;
    int address_size;
    Cacheline[] real_cache;
    int missedAddress;
    Element missedElement;
    boolean isRead;
    int writeData;

    public Cache(int size, Processor processor, int wordSize, int WordsPerLine, int count_lines_in_set,
            int address_size, int latency) {
        // Order of initializations matter here
        containingProcessor = processor;
        this.cache_size = size;
        this.word_size = wordSize;
        this.count_words_in_line = WordsPerLine;
        this.associativity = count_lines_in_set;
        this.address_size = address_size;
        this.cache_latency = latency;
        this.lines_count = (this.cache_size) / (word_size * count_words_in_line);
        this.real_cache = new Cacheline[this.lines_count];
        for (int i = 0; i < lines_count; i++) {
            real_cache[i] = new Cacheline(count_words_in_line, this.word_size);
        }
    }

    public int get_latency() {
        return this.cache_latency;
    }

    // Number of reuired bits for representation of (n-1)
    // But count_bits(0) = 0
    int count_bits(int n) {
        int index_bits_count = 0;
        int temp = n;
        while (temp > 0) {
            index_bits_count++;
            temp = temp >>> 1;
        }

        // If number is power of 2
        if ((n == (1 << (index_bits_count - 1))))
            index_bits_count--;
        return index_bits_count;
    }

    public int cacheRead(int address, Element requestElement) {
        int index_bit_count = count_bits(lines_count);
        int index_bit_number = address & ((1 << index_bit_count) - 1);
        int cache_tag = real_cache[index_bit_number].get_tag();
        if (cache_tag == address) {
            Simulator.getEventQueue().addEvent(
                    new MemoryResponseEvent(
                            Clock.getCurrentTime(),
                            this,
                            requestElement,
                            real_cache[index_bit_number].get_data()));
        } else {
            isRead = true;
            handleCacheMiss(address, requestElement);
        }
        return 0;
    }

    public void cacheWrite(int address, int value, Element requestingElement) {
        int index_bit_count = count_bits(lines_count);
        int index_bit_number = address & ((1 << index_bit_count) - 1);
        int cache_tag = real_cache[index_bit_number].get_tag();
        if (cache_tag == address) {
            real_cache[index_bit_number].set_data(value);
            Simulator.getEventQueue().addEvent(
                    new MemoryWriteEvent(
                            Clock.getCurrentTime(),
                            this,
                            containingProcessor.getMainMemory(),
                            address,
                            value));
            ((MemoryAccess) requestingElement).EX_MA_Latch.set_is_MA_busy(false);
            ((MemoryAccess) requestingElement).MA_RW_Latch.setRW_enable(true);
        } else {
            isRead = false;
            writeData = value;
            handleCacheMiss(address, requestingElement);
        }
    }

    public void handleCacheMiss(int address, Element requestElement) {
        Simulator.getEventQueue().addEvent(
                new MemoryReadEvent(
                        Clock.getCurrentTime() + Configuration.mainMemoryLatency,
                        this,
                        this.containingProcessor.getMainMemory(),
                        address));
        missedAddress = address;
        missedElement = requestElement;
    }

    public void handleResponse(int value) {
        int index_bit_count = count_bits(lines_count);
        int index_bit_number = missedAddress & ((1 << index_bit_count) - 1);
        real_cache[index_bit_number].set_data(value);
        real_cache[index_bit_number].set_tag(missedAddress);
        if (isRead) {
            Simulator.getEventQueue().addEvent(
                    new MemoryResponseEvent(
                            Clock.getCurrentTime(),
                            this,
                            missedElement,
                            value));
        } else {
            cacheWrite(missedAddress, writeData, missedElement);
        }
    }

    @Override
    public void handleEvent(Event e) {
        if (e.getEventType() == EventType.MemoryResponse) {
            MemoryResponseEvent event = (MemoryResponseEvent) e;
            handleResponse(event.getValue());
        } else if (e.getEventType() == EventType.MemoryRead) {
            MemoryReadEvent event = (MemoryReadEvent) e;
            cacheRead(event.getAddressToReadFrom(), event.getRequestingElement());
        } else if (e.getEventType() == EventType.MemoryWrite) {
            MemoryWriteEvent event = (MemoryWriteEvent) e;
            cacheWrite(event.getAddressToWriteTo(), event.getValue(), event.getRequestingElement());
        }
    }
}
