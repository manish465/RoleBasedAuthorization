local typedefs = require "kong.db.schema.typedefs"

return {
    name = "auth-check",
    fields = {
        { consumer = typedefs.no_consumer }, -- optional, good practice
        { config = {
            type = "record",
            fields = {
                { auth_service_url = { type = "string", required = true, default = "http://host.docker.internal:9010" } },
            },
        },
        },
    }
}
