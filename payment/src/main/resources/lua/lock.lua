local key = KEYS[1]
--- application_name-instance_id-thread_id with "-"
local app_id_td = ARGV[1]
local value = redis.call('GET', key)
local timeout = ARGV[2]
local result = 0
if value then
    local str = string.sub(value, 1, string.len(app_id_td))
    if str == app_id_td then
        local count = tonumber(string.sub(value, string.len(app_id_td) + 1))
        count = count + 1
        local new_value = app_id_td .. tostring(count)
        redis.call('PSETEX', key, timeout, new_value)
        result = count
    end
else
    local count = "1"
    local new_value = app_id_td .. count
    redis.call('PSETEX', key, timeout, new_value)
    result = 1
end

return tostring(result)

