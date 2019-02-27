#!/bin/sh

# Create a new image version with latest code changes.
docker build . --tag pleo-antaeus


read YYYY MM DD <<<$(date +'%Y %m %d')
CURRENT_DAY=$DD

TEST_ENV=false

docker run \
  --name pleo-anteus-app \
  --publish 7000:7000 \
  --rm \
  --interactive \
  --tty \
  --volume pleo-antaeus-build-cache:/home/pleo/.gradle \
  pleo-antaeus
