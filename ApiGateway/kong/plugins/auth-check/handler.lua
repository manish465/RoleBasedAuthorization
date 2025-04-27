local http = require "resty.http"

local AuthCheck = {
    PRIORITY = 1000,
    VERSION = "1.0.0"
}

local PUBLIC_PATHS = {
    ["/api/v1/users/add-user"] = true,
    ["/api/v1/users/get-user"] = true,
    ["/api/v1/users/delete-all-users"] = true,
    ["/api/v1/users/health-check"] = true,
    ["/api/v1/auth/sign-up"] = true,
    ["/api/v1/auth/sign-in"] = true,
    ["/api/v1/auth/health-check"] = true,
    ["/api/v1/auth/create-jwt"] = true,
    ["/api/v1/auth/validate-user"] = true
}

function AuthCheck:access(conf)
    -- Skip validation for public paths
    local path = kong.request.get_path()
    if PUBLIC_PATHS[path] then
        return -- Allow access for public paths
    end

    -- Validate token for protected paths
    local token = kong.request.get_header("Authorization")
    if not token then
        return kong.response.exit(401, { message = "No authorization token provided" })
    end

    token = token:gsub("Bearer ", "")

    local httpc = http.new()
    local res, err = httpc:request_uri(conf.auth_service_url .. "/api/v1/auth/validate-user", {
        method = "GET",
        query = {
            token = token,
            path = path
        },
        headers = {
            ["Content-Type"] = "application/json"
        }
    })

    if not res or res.status ~= 200 then
        return kong.response.exit(403, { message = "Access denied" })
    end
end

return AuthCheck