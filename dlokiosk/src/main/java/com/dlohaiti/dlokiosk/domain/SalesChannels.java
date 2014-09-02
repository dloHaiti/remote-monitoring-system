package com.dlohaiti.dlokiosk.domain;

import java.util.ArrayList;
import java.util.Collection;

public class SalesChannels extends ArrayList<SalesChannel> {

    public SalesChannels(Collection<SalesChannel> channels) {
        super(channels);
    }

    public SalesChannel findSalesChannelById(long id) {
        for (SalesChannel salesChannel : this) {
            if (salesChannel.id() == id) {
                return salesChannel;
            }
        }
        throw new NoSalesChannelWithGivenIdException(id);
    }

}
