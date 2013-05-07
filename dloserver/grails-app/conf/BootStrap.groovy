import com.dlohaiti.dloserver.Location

class BootStrap {

    def init = { servletContext ->
        if (Location.count() == 0) {
            new Location(name: 'Borehole').save()
            new Location(name: 'WTU Eff').save()
            new Location(name: 'WTU Feed').save()
        }
    }
    def destroy = {
    }
}
