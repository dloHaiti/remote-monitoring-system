package com.dlohaiti.dlokiosk.domain;

import com.dlohaiti.dlokiosk.exception.NoSalesChannelWithGivenIdException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    public SalesChannel findSalesChannelByName(String name) {
        for (SalesChannel salesChannel : this) {
            if (salesChannel.name().equalsIgnoreCase(name)) {
                return salesChannel;
            }
        }
        return null;
    }

    public List<String> getSalesChannelNames() {
        List<String> names = new ArrayList<String>();
        for (SalesChannel sc : this) {
            names.add(sc.name());
        }
        return names;
    }

    public List<SalesChannel> getSalesChannelsFromName(List<String> names) {
        List<SalesChannel> channels = new ArrayList<SalesChannel>();
        for (String name : names) {
            SalesChannel sc = this.findSalesChannelByName(name);
            if (sc != null) {
                channels.add(sc);
            }
        }
        return channels;
    }
}
