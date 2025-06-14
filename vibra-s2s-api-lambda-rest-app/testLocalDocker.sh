event_name=${1}
curl -X POST "http://localhost:3333/2015-03-31/functions/function/invocations" -d "@docker/events/${event_name}Event.json"

# bash testLocalDocker.sh hello
# bash testLocalDocker.sh start-game-auth-megafair-ok