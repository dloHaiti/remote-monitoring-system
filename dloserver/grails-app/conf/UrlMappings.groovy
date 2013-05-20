class UrlMappings {

    static mappings = {
        "/reading/$id?"(resource: "reading")
        "/receipts/$id?"(resource: "receipts")
        "/deliveries/$id?"(resource: "deliveries")
        "/healthcheck/$id?"(resource: "healthcheck")

        "/$controller/$action?/$id?" {
            constraints {
                // apply constraints here
            }
        }

        "/"(view: "/index")
        "500"(view: '/error')
    }
}
