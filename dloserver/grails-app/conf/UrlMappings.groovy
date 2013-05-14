class UrlMappings {

    static mappings = {
        "/reading/$id?"(resource: "reading")
        "/sales/$id?"(resource: "sales")
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
