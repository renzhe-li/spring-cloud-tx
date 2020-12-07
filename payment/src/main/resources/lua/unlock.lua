local key = KEYS[1]
--- application_name-instance_id-thread_id with "-"
local app_id_td = ARGV[1]
local value = redis.call('GET', key)
local result = 0
if value then
    local str = string.sub(value, 1, string.len(app_id_td))
    if str == app_id_td then
        local count = tonumber(string.sub(value, string.len(app_id_td) + 1))
        if count <= 1 then
            redis.call('del', key)
        else
            count = count - 1
            local new_value = app_id_td .. tostring(count)
            local pttl = redis.call('PTTL', key)
            redis.call('PSETEX', key, pttl, new_value)
        end

        result = 1
    end
else
    result = 1
end

return tostring(result)
