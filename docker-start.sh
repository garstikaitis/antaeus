#!/bin/sh

# Create a new image version with latest code changes.
docker build . --tag pleo-antaeus


docker run \
  -v
  --name pleo-anteus-app \
  --publish 7000:7000 \
  --rm \
  --interactive \
  --tty \
  --volume pleo-antaeus-build-cache:/home/pleo/.gradle \
  pleo-antaeus
