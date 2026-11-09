#!/bin/sh
set -eu

PROJECT_ROOT=$(CDPATH= cd -- "$(dirname -- "$0")/.." && pwd)
cd "$PROJECT_ROOT"

export DOCKERHUB_USERNAME="${DOCKERHUB_USERNAME:-oualidteh}"
export IMAGE_TAG="${1:-latest}"

docker compose build entity-manager user-service company-service application-service reminder-service web-gateway frontend
docker compose push entity-manager user-service company-service application-service reminder-service web-gateway frontend
