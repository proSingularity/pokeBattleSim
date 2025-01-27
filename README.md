# pokeBattleSim

Desctop application to let two teams of classical pokemon fight against each other.

Backend: Java (Spring)
Frontend: Typescript

## Deployment

### Prerequisites

- Ensure Docker is installed.
- copy .env.sample to .env and fill in the values.

### Main

Then run from repository root:

```bash
cd web && npm ci && npm run build && cd ..
docker build -t poke-battle-sim .

# possibly remove old container
docker container rm poke-battle-sim

# Option A: run with secrets mounted
export PROJECT_ROOT=$(pwd)

docker run -p 8080:8080 --env-file ./.env --name poke-battle-sim --mount type=bind,source=${PROJECT_ROOT}/docker,target=/usr/src/app/mnt/secrets poke-battle-sim

# alternative Option B: run without mounting secrets
docker run -p 8080:8080 --name poke-battle-sim poke-battle-sim
```
