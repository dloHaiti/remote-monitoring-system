import com.dlohaiti.dloserver.Location

class BootStrap {

    def init = { servletContext ->
        if (Location.count() == 0) {
            new Location(name: 'BOREHOLE').save()
            new Location(name: 'WTU EFF').save()
            new Location(name: 'WTU FEED').save()
        }
    }
    def destroy = {
    }
}
