local key = KEYS[1]
local limit = tonumber(ARGV[1])
local time = tonumber(ARGV[2])

local currentLimit = redis.call('INCR', key)
if currentLimit == 1 then
    redis.call("EXPIRE", key, time)
end

if currentLimit > limit then
    return {0,currentLimit}
else
    return {1,currentLimit}
end